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

    var isAttached = false

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        isAttached = true

        updateNotifToken()
    }

    fun startUpdatingPings(){
        doAsync {
            var isAttached = true

            while (isAttached){
                if(isAttached)
                    uiThread {
                        compositeDisposable.add(mRepo.getMapPings()
                                .subscribe({pings: ArrayList<Action>? ->
                                    Log.d("tytyty","asdasd")
                                    if (pings != null){
                                        Log.d("pingspings",pings.toString() + "asd")
                                        val pingsNew = ArrayList<Ping>()
                                        for (action in pings){
                                            if (action.type == "ping")
                                                pingsNew.add(Ping(action))
                                        }
                                        view.updatePings(pingsNew,true)
                                    }
                                },{t: Throwable? ->
                                    Log.d("updating actions: ","AAA")
                                    Log.d("updating actions: ",t!!.message)
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

    fun updateNotifToken(){
        val userToken = mRepo.prefs.getUserToken()
        val firebaseToken = FirebaseInstanceId.getInstance().token ?: ""

        compositeDisposable.add(mRepo.rest.networkService.updateNotifToken(userToken, UpdateTokenRequest(firebaseToken))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("updateNotifToken",t)
                },{t: Throwable? ->
                    Log.d("updateNotifToken",t!!.message)
                }))
    }

    override fun onCircleClick(visibility: Int) {
        if(visibility == View.GONE)
            view.animateExtendedCircle(true)
        else{
            view.animateExtendedCircle(false)
            view.animateTabLayout(false)
            view.showSlide("addTask")
        }
    }

    override fun onOrganiserButtonClick() {
        view.showSlide("organizer")
        view.animateExtendedCircle(false)
    }

    override fun onOrganizerClick(organizerId: String) {
        val data =  HashMap<String,Any>()
        data["organizerId"] = organizerId
        data["callLocation"] = mCurrentLocation

        Log.d("onOrganizerClick",data.toString())

        val token = mRepo.prefs.getUserToken()

        compositeDisposable.add(mRepo.rest.networkService.sendPingToOrganizer(token, data)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("onOrganizerClick",t.orEmpty())
                },{t: Throwable? ->
                    Log.d("onOrganizerClick",t.toString())
                }))
    }

    override fun onHelpClick() {
        view.showMessage("Wysłano prośbę o pomoc!",MvpView.MessageType.SUCCESS)
        view.animateExtendedCircle(false)
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
        compositeDisposable.add(mRepo.createAction(action).subscribe({ t: String? ->
            listener.clearData()
            view.showMessage("Dodano zadanie!",MvpView.MessageType.SUCCESS)
            view.hideSlide()
            view.removeFragment("addTask")
            view.animateTabLayout(true)
        },{ t: Throwable? ->
            view.showMessage(t?.localizedMessage.orEmpty(),MvpView.MessageType.ERROR)
            view.hideSlide()
            view.animateTabLayout(true)
        }))
    }

    override fun onAddButtonClick(checkedGroups: ArrayList<String>, date: String) {
        Log.d("groupss",checkedGroups.toString())

        if(checkedGroups.isEmpty() && date.isEmpty()){
            view.showMessage("Wybierz co najmniej jedną grupę",MvpView.MessageType.INFO)
            return
        }

        if(typeOfNewThing == "ping"){
            if(checkedGroups.isNotEmpty())
                mRepo.newPing.targetGroups = checkedGroups
            mRepo.newPing.date = date
            compositeDisposable.add(mRepo.createPing()
                    .subscribe({t: String? ->
                        //if (date.isEmpty())
                           // view.addPing(mRepo.newPing)
                        view.showMessage("Stworzono ping",MvpView.MessageType.SUCCESS)
                        view.hideCreatePingView()
                    },{t: Throwable? ->
                        Log.d("error",t!!.message.orEmpty())
                        view.showMessage("Nie udało się stworzyć pingu",MvpView.MessageType.ERROR)
                        view.hideCreatePingView()
                    }))
        }
        else{
            mRepo.newInfo.date = date
            if(checkedGroups.isNotEmpty())
                mRepo.newInfo.targetGroups = checkedGroups
            compositeDisposable.add(mRepo.createInfo()
                    .subscribe({t: String? ->
                        val message = if(date.isNotEmpty()) "Zaplanowano" else "Stworzono"
                        view.showMessage("$message info",MvpView.MessageType.SUCCESS)
                        view.hideCreatePingView()
                    },{t: Throwable? ->
                        Log.d("error",t!!.message.orEmpty())
                        view.showMessage("Nie udało się stworzyć informacji",MvpView.MessageType.ERROR)
                        view.hideCreatePingView()
                    }))
        }
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

    override fun onMapLongClick(location: LatLng) {
        typeOfNewThing = "ping"
        mRepo.type = typeOfNewThing
        mRepo.newPing.geo.add(location.latitude)
        mRepo.newPing.geo.add(location.longitude)
        view.showCreatePingView("ping")
    }

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
        typeOfNewThing = "ping"
        mRepo.type = typeOfNewThing
        mRepo.newPing.geo.clear()
        mRepo.newPing.geo.add(location.latitude)
        mRepo.newPing.geo.add(location.longitude)
        view.showCreatePingView("ping")
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
        view.showCreatePingView("info")
    }

    override fun onProfileClick() {
        view.showFullFragments("profile")
    }

    override fun onHistoryButtonClick() {
        view.showSlide("history")
    }

    override fun clearData() {
        mRepo.clearData()
    }

    override fun onEndPingClick(id: String) {
        Log.d("idsss",id)
        mRepo.endPing(id)
                .subscribe({response: String? ->
                    Log.d("koko",response.orEmpty())
                    view.removePing(id)
                    view.showMessage("Zadanie wykonane",MvpView.MessageType.SUCCESS)
                },{ t: Throwable? ->
                    view.showMessage("Wykonanie zadania nie powiodło się",MvpView.MessageType.ERROR)
                    Log.d("koko",t!!.message.orEmpty())
                })
    }

    override fun onInProgressClick(id: String) {
        Log.d("idsss",id)
        mRepo.inProgressPing(id)
                .subscribe({response: String? ->
                    Log.d("koko",response.orEmpty())
                    view.showMessage("Powodzenia !",MvpView.MessageType.SUCCESS)
                },{ t: Throwable? ->
                    view.showMessage("Problem ze zmianą statusu zadania",MvpView.MessageType.ERROR)
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
        if(mRepo.facebook.isLoggedIn())
            mRepo.facebook.logOut()
        mRepo.prefs.removeUser()

        view.openLoginActivity()

    }

}