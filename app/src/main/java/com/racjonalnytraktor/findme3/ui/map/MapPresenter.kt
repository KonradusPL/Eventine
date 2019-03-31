package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import android.util.Log
import android.view.View
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.UpdateTokenRequest
import com.racjonalnytraktor.findme3.data.model.map.ZoneUpdate
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.ui.map.listeners.Listener
import com.racjonalnytraktor.findme3.utils.MapHelper
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V>
,MapHelper.MapListener{

    private var mRepo =  MapRepository
    private val mMembers: ArrayList<Job> = ArrayList()

    private var actionRequest = CreateActionRequest()
    private var typeOfNewThing = "ping"
    private var isChoosingLocation = false
    private var mLocationListener: Listener.ChangeLocation? = null
    private var mCurrentLocation = "Pokoik"

    private var mFloorIndex = 1
    private val mFloors = arrayListOf(-1,0,1)

    var isAttached = false

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        isAttached = true

        val user = mRepo.prefs.getCurrentUser()
        Log.d("user data: ",user.toString())

        /*val token = mRepo.prefs.getUserToken()
        val map = HashMap<String,Any>()
        map["groupId"] = mRepo.prefs.getCurrentGroupId()
        map["locationTag"] = "Pokoik"
        mRepo.rest.networkService.updateLocation(token,map)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("updateLocation",t)
                },{t: Throwable? ->
                    Log.d("updateLocation",t.toString())
                })*/

        if(mRepo.prefs.isPartner())
            view.updateOnPartner()

        mRepo.updateMembers()

        updateNotifToken()
    }

    fun startUpdatingMap(){
        doAsync {
            var isAttached = true

            while (isAttached){
                if(isAttached)
                    uiThread {
                        compositeDisposable.add(mRepo.getMapPings()
                                .subscribe({pings: ArrayList<Action>? ->
                                    if (pings != null){
                                        val pingsNew = ArrayList<Ping>()
                                        for (action in pings){
                                            if (action.type == "ping"){
                                                pingsNew.add(Ping(action))
                                            }
                                        }
                                        view.updatePings(pingsNew,mFloors[mFloorIndex])
                                    }
                                },{t: Throwable? ->
                                }))
                        compositeDisposable.add(mRepo.getZonesWithUserCount()
                                .subscribe({t: ArrayList<ZoneUpdate>? ->
                                    Log.d("getZonesWithUserCount",t.toString())
                                    if(t != null)
                                        view.updateZones(t)
                                },{t: Throwable? ->
                                    Log.d("getZonesWithUserCount",t.toString())
                                }))


                    }


                Log.d("isAttached",isAttached.toString())
                for(i in 0..50){
                    if(!view.isAttached()){
                        isAttached = false
                        Log.d("tututu","tututu")
                        break
                    }
                    Thread.sleep(100)
                }
            }
        }
    }

    private fun updateNotifToken(){
        val userToken = mRepo.prefs.getUserToken()
        val firebaseToken = FirebaseInstanceId.getInstance().token ?: ""
        Log.d("updateNotifToken",firebaseToken)

        compositeDisposable.add(mRepo.rest.networkService.updateNotifToken(userToken, UpdateTokenRequest(firebaseToken))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("updateNotifToken",t)
                },{t: Throwable? ->
                    Log.d("updateNotifToken",t!!.message)
                }))
    }

    override fun onFloorSelected(floor: Int) {
        mFloorIndex = floor
    }

    override fun onCircleClick(visibility: Int) {
        if(visibility == View.GONE)
            view.animateExtendedCircle(true)
        else{
            view.clearTab(0,true)
            view.hideFullFragments()
            view.animateExtendedCircle(false)
            view.animateTabLayout(false)
            view.showSlide("addTask")
        }
    }

    override fun onOrganiserButtonClick() {
        view.clearTab(0,true)
        view.showSlide("organizer")
        view.animateExtendedCircle(false)
    }

    override fun onOrganizerClick(organizerId: String) {
        val data =  HashMap<String,Any>()
        data["organizerId"] = organizerId
        data["callLocation"] = mCurrentLocation

        Log.d("onOrganizerClick",data.toString())

        view.showLoading()

        val token = mRepo.prefs.getUserToken()

        compositeDisposable.add(mRepo.rest.networkService.sendPingToOrganizer(token, data)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    view.hideLoading()
                    view.showMessage("Wysłano help do organizatora",MvpView.MessageType.SUCCESS)
                    Log.d("onOrganizerClick",t.orEmpty())
                },{t: Throwable? ->
                    view.hideLoading()
                    view.showMessage("Nie udało się wysłać helpu",MvpView.MessageType.ERROR)
                    Log.d("onOrganizerClick",t.toString())
                }))
    }

    override fun onHelpClick() {
        val token = mRepo.prefs.getUserToken()
        val data = HashMap<String,String>()
        data["groupId"] = mRepo.prefs.getCurrentGroupId()
        Log.d("onHelpClick",data.toString())

        view.clearTab(0,true)

        view.showLoading()

        val single = if (mRepo.prefs.isPartner()) mRepo.rest.networkService.sendPingToNearest(token, data)
            else mRepo.rest.networkService.callCareTaker(token)


        compositeDisposable.add(single
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ t: String? ->
                    view.hideLoading()
                    view.showMessage("Wysłano prośbę o pomoc!",MvpView.MessageType.SUCCESS)
                    view.animateExtendedCircle(false)
                },{t: Throwable? ->
                    view.hideLoading()
                    view.animateExtendedCircle(false)
                }))
    }

    override fun onNextButtonClick(task: String, descr: String) {
        if(mRepo.type == "ping"){
            mRepo.newPing.title = task
            mRepo.newPing.desc = descr
        }
        else
            mRepo.newInfo.content = descr

        getAllSubGroups()
    }

    override fun onSilentSwitch(value: Boolean) {
        mRepo.prefs.setIsSilentNotification(value)
    }

    fun getAllSubGroups(){
        mRepo.getAllSubGroups("","")
                .flatMapIterable { t -> t }
                .doOnComplete {
                    if (mRepo.type == "ping")
                        view.updateCheckedGroups(mRepo.newPing.targetGroups)
                    else
                        view.updateCheckedGroups(mRepo.newInfo.targetGroups)
                }
                .subscribe({t: String? ->
                    Log.d("asdasdasd",t!!.toString())
                    view.updateSubGroups(t)
                },{t: Throwable? ->
                    Log.d("asdasdasd",t?.message.orEmpty())
                })
    }

    override fun onCreateActionClick(action: CreateActionRequest, listener: Listener.CreateAction) {
        Log.d("onCreateActionClick",action.plannedTime)
        Log.d("onCreateActionClick",action.title)
        Log.d("onCreateActionClick",action.desc)
        Log.d("onCreateActionClick",action.type)

        action.floor = mFloors[mFloorIndex]

        Log.d("onCreateActionClick",action.floor.toString())

        Log.d("onCreateActionClickTo",action.toString())

        view.showLoading()
        compositeDisposable.add(mRepo.createAction(action).subscribe({ t: String? ->
            listener.clearData()
            view.hideLoading()
            view.showMessage("Dodano zadanie!",MvpView.MessageType.SUCCESS)
            view.hideSlide()
            view.removeFragment("addTask")
            view.animateTabLayout(true)
        },{ t: Throwable? ->
            view.hideLoading()
            view.showMessage(t?.localizedMessage.orEmpty(),MvpView.MessageType.ERROR)
            view.hideSlide()
            view.animateTabLayout(true)
        }))
    }

    override fun onPlanButtonClick(checkedGroups: ArrayList<String>) {
        if(checkedGroups.isEmpty()){
            view.showMessage("Wybierz co najmniej jedną grupę",MvpView.MessageType.INFO)
            return
        }

        if(mRepo.type == "ping")
            mRepo.newPing.targetGroups = checkedGroups
        else
            mRepo.newPing.targetGroups = checkedGroups

        view.showPlanDialog()

    }

    override fun onLogoutButtonClick() {
        if(AccessToken.isCurrentAccessTokenActive())
            LoginManager.getInstance().logOut()
        mRepo.prefs.removeUser()
        view.openLoginActivity()
    }

    fun onChangeGroupClick(groupName: String) {

        for (group in mRepo.appRepo.groups){
            if(group.groupName == groupName){
                mRepo.prefs.apply {
                    setCurrentGroupName(groupName)
                    setCurrentGroupId(group.id)
                }
                view.openMapActivity()
                break
            }
        }
    }

    override fun onMapLongClick(location: LatLng) {}

    override fun onMapClick(location: Location) {
        if(isChoosingLocation){
            isChoosingLocation = false
            actionRequest.geo[0] = location.latitude
            actionRequest.geo[1] = location.longitude
            mLocationListener?.changeLocation(location)
            view.showSlide("addTask")
            view.animateTabLayout(false)
        }
        view.animateExtendedCircle(false)
    }

    override fun onMarkerClick(ping: Ping) {
        view.showEndPingBar(ping)
    }

    override fun onLongClickListener(location: LatLng) {
        val newLocation = Location("")
        newLocation.latitude = location.latitude
        newLocation.longitude = location.longitude
        view.hideFullFragments()
        view.animateExtendedCircle(false)
        view.animateTabLayout(false)
        view.showSlide("addTask",newLocation)
    }

    override fun onDetach() {
        super.onDetach()
        isAttached = false
        compositeDisposable.clear()
       // mRepo.locationProvider.end()
    }

    override fun onMapPrepared() {
        /*compositeDisposable.add(mRepo.getActions()
                .subscribe({ping: Ping? ->
                    Log.d("wwwww",ping!!.pingId)
                    Log.d("actions","asdasd")
                    if (ping != null)
                        view.updatePings(ping)
                },{t: Throwable? ->
                    Log.d("error",t!!.message)
                }))*/
    }

    override fun onSavingState(checked: List<String>, task: String, descr: String, state: String) {
        mRepo.saveState(checked,task,descr,typeOfNewThing,state)
    }

    override fun onInfoTabClick() {
        typeOfNewThing = "info"
        mRepo.type = typeOfNewThing
    }

    override fun onProfileClick() {
        view.showFullFragments("profile")
    }

    override fun onHistoryButtonClick() {
        view.hideFullFragments()
        view.showSlide("history")
    }

    override fun clearData() {
        mRepo.clearData()
    }

    override fun onEndPingClick(id: String) {
        Log.d("idsss",id)
        view.showLoading()
        mRepo.endPing(id)
                .subscribe({response: String? ->
                    Log.d("koko",response.orEmpty())
                    view.hideLoading()
                    view.removePing(id)
                    view.showMessage("Zadanie wykonane",MvpView.MessageType.SUCCESS)
                },{ t: Throwable? ->
                    view.hideLoading()
                    view.showMessage("Wykonanie zadania nie powiodło się",MvpView.MessageType.ERROR)
                    Log.d("koko",t!!.message.orEmpty())
                })
    }

    override fun onInProgressClick(id: String) {
        Log.d("idsss",id)
        view.showLoading()
        mRepo.inProgressPing(id)
                .subscribe({response: String? ->
                    Log.d("koko",response.orEmpty())
                    view.hideLoading()
                    view.showMessage("Powodzenia !",MvpView.MessageType.SUCCESS)
                },{ t: Throwable? ->
                    view.showMessage("Problem ze zmianą statusu zadania",MvpView.MessageType.ERROR)
                    view.hideLoading()
                    Log.d("koko",t!!.message.orEmpty())
                })
    }

    override fun onManageGroupAttach(listener: Listener.Manage) {
        listener.showLoading()
        compositeDisposable.add(mRepo.getMembers()
                .subscribe({t: ArrayList<Job>? ->
                    listener.hideLoading()
                    listener.showList(t ?: emptyList())
                    mMembers.clear()
                    mMembers.addAll(t.orEmpty())
                },{t: Throwable? ->
                    Log.d("onManageGroupAttach",t.toString())
                    listener.hideLoading()
                    if(mMembers.isNotEmpty())
                        listener.showList(mMembers)
                    else
                        listener.onError()
                }))
    }

    override fun onGroupsClick() {
        view.showFullFragments("groups")
    }

    override fun onOptionsClick() {
        view.showFullFragments("options")
    }

    override fun onBackInFragmentClick(type: String) {
        view.hideFullFragments(type,true)
        view.hideSlide()
        view.animateExtendedCircle(false)
    }

    override fun onChangeLocationClick(locationListener: Listener.ChangeLocation) {
        isChoosingLocation = true
        mLocationListener = locationListener
        view.showMessage("Wybierz położenie pingu",MvpView.MessageType.INFO)
        view.hideSlide()
    }

    override fun onSlideHide() {
        if(!isChoosingLocation)
            view.animateTabLayout(true)
    }

    override fun onAddTaskListAttach(listener: Listener.AddTaskList) {
        listener.showListLoading()
        compositeDisposable.add(mRepo.getMembers()
                .subscribe({t: ArrayList<Job>? ->
                    listener.hideListLoading()
                    listener.showList(t ?: arrayListOf())
                },{t: Throwable? ->
                    listener.hideListLoading()
                    listener.showError()
                }))
    }

    override fun onOrganisersAttach(listener: Listener.Organisers) {
        listener.showLoading()
        compositeDisposable.add(mRepo.getMembers()
                .subscribe({t: ArrayList<Job>? ->
                    listener.hideLoading()
                    listener.showList(t ?: arrayListOf())
                },{t: Throwable? ->
                    listener.hideLoading()
                }))
    }

    override fun onZoneEnter(zone: String) {
        val token = mRepo.prefs.getUserToken()
        val map = HashMap<String,Any>()
        map["groupId"] = mRepo.prefs.getCurrentGroupId()
        map["locationTag"] = zone
        compositeDisposable.add(mRepo.rest.networkService.updateLocation(token,map)
                .subscribe({t: String? ->
                    Log.d("updateLocation",t.toString())
                },{t: Throwable? ->
                    Log.d("updateLocation",t.toString())
                }))
    }

    override fun onLogOutClick() {
        view.changeBeaconsStatus(false)

        if(mRepo.facebook.isLoggedIn())
            mRepo.facebook.logOut()
        mRepo.prefs.removeUser()

        view.openLoginActivity()

    }

}