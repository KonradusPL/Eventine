package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import android.util.Log
import android.view.View
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.Worker
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.MapHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V>
,MapHelper.MapListener{

    var actionRequest = CreateActionRequest()

    var isAttached = false

    var mRepo =  MapRepository

    var typeOfNewThing = "ping"

    var isChoosingLocation = false

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        isAttached = true

        val checked: List<String>
        val task: String
        val descr: String

        if(mRepo.type == "ping"){
            checked = mRepo.newPing.targetGroups.toList()
            task = mRepo.newPing.title
            descr = mRepo.newPing.desc
        }
        else{
            checked = mRepo.newInfo.targetGroups
            task = ""
            descr = mRepo.newPing.desc
        }

        view.updateWithSavedData(task, descr, checked,mRepo.type,mRepo.state)
    }

    fun startUpdatingPings(){
        doAsync {
            var isAttached = true

            while (isAttached){

                Log.d("logus","isAttached")
                    uiThread {
                        //view.clearPings()
                    }
                    if(isAttached)
                        compositeDisposable.add(mRepo.getPings()
                                .subscribe({pings: List<Ping>? ->
                                    Log.d("pings",pings.toString())
                                    if (pings != null)
                                        view.updatePings(pings,true)
                                },{t: Throwable? ->
                                    Log.d("error",t!!.message)
                                }))

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

    override fun onCircleClick(visibility: Int) {
        if(visibility == View.GONE)
            view.animateExtendedCircle(true)
        else{
            view.animateExtendedCircle(false)
            view.animateTabLayout(false)
            view.showSlide("addTask")
        }
    }

    override fun onOrganiserClick() {
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

    override fun onCreateActionClick(action: CreateActionRequest) {
        view.showMessage("Dodano zadanie!",MvpView.MessageType.SUCCESS)
        view.hideSlide()
        view.animateTabLayout(true)
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
       // mRepo.locationProvider.end()
    }

    override fun onMapPrepared() {
        /*compositeDisposable.add(mRepo.getPings()
                .subscribe({ping: Ping? ->
                    Log.d("wwwww",ping!!.pingId)
                    Log.d("pings","asdasd")
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

    override fun onHistoryButtonClick() {
        view.showSlide("history")
    }

    override fun clearData() {
        mRepo.clearData()
    }

    override fun onEndPing(id: String) {
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

    override fun onManageGroupAttach() {
        Log.d("presenter","onManageGroupAttach")
        val workers = ArrayList<Worker>()
        //val worker = Worker("Marcin Michno")
        val jobs = ArrayList<Job>()
       /* workers.add(Worker("Jan Kowalski"))
        workers.add(Worker("Mateusz Zawada"))
        workers.add(Worker("John Doe"))
        workers.add(Worker("Ewelina Nowak"))
        workers.add(Worker("Ryszard Mularski"))
        workers.add(Worker("Martyna Kawa"))*/

        val stringArray = arrayListOf("Organizator","MC","Logistyka","Marketing & PR","Sprzedaż","Serwis")



        for(i in 0..5){
            val job = Job(stringArray[i],6,workers)
            jobs.add(job)
        }
        view.showManageGroupList(jobs)
    }

    override fun onGroupsClick() {
        view.showFullFragments("groups")
    }

    override fun onOptionsClick() {
        view.showFullFragments("options")
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

    override fun onBackInFragmentClick(type: String) {
        view.hideFullFragments(type,true)
        view.hideSlide()
        view.animateExtendedCircle(false)
    }

    override fun onChangeLocationClick() {
        isChoosingLocation = true
        view.hideSlide()
    }

    override fun onSlideHide() {
        if(!isChoosingLocation)
            view.animateTabLayout(true)
    }

}