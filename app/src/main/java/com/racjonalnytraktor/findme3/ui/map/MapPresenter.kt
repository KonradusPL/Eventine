package com.racjonalnytraktor.findme3.ui.map

import android.util.Log
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V> {

     var mRepo =  MapRepository

    var typeOfNewThing = "ping"

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        Log.d("rew","1")

        mRepo.onAttatch(mvpView.getCtx())

        val checked: List<String>
        val task: String
        val descr: String

        Log.d("rew","2")


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

        Log.d("rew","3")

        var toolbarName = mRepo.prefs.getCurrentGroupName()
        if(toolbarName == "null")
            toolbarName = "Kalejdoskop"
        view.changeToolbarName(toolbarName)

        view.setUpLeftNavigation(mRepo.appRepo.groups)

        Log.d("rew","4")


        view.updateWithSavedData(task, descr, checked,mRepo.type,mRepo.state)

        /*view.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { state: PermissionsHelper.PermissionState? ->
                    if (state == PermissionsHelper.PermissionState.GRANTED)
                        view.checkLocationSettings()
                                .subscribe {
                                   // mRepo.locationProvider.start()
                                }
                }*/
    }

    override fun onNextButtonClick(task: String, descr: String) {
        view.changeCreateGroupFragment()
        if(typeOfNewThing == "ping"){
            mRepo.newPing.title = task
            mRepo.newPing.desc = descr

        }
        else
            mRepo.newInfo.content = descr

        getAllSubGroups()
    }

    fun getAllSubGroups(){
        mRepo.getAllSubGroups()
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

    override fun onAddButtonClick(checkedGroups: ArrayList<String>) {
        if(typeOfNewThing == "ping"){
            mRepo.newPing.targetGroups = checkedGroups
            compositeDisposable.add(mRepo.createPing()
                    .subscribe({t: String? ->
                        view.updatePings(mRepo.newPing)
                        view.showMessage("SUCCESS PING",MvpView.MessageType.SUCCESS)
                        view.hideCreatePingView()
                    },{t: Throwable? ->
                        Log.d("error",t!!.message.orEmpty())
                        view.showMessage("ERROR :(",MvpView.MessageType.ERROR)
                        view.hideCreatePingView()
                    }))
        }
        else{
            mRepo.newInfo.targetGroups = checkedGroups
            compositeDisposable.add(mRepo.createInfo()
                    .subscribe({t: String? ->
                        view.showMessage("SUCCESS INFO",MvpView.MessageType.SUCCESS)
                        view.hideCreatePingView()
                    },{t: Throwable? ->
                        Log.d("error",t!!.message.orEmpty())
                        view.showMessage("ERROR :(",MvpView.MessageType.ERROR)
                        view.hideCreatePingView()
                    }))
        }
    }

    override fun onLogoutButtonClick() {
        if(AccessToken.isCurrentAccessTokenActive())
            LoginManager.getInstance().logOut()
        mRepo.prefs.setCurrentUser(User())
        mRepo.prefs.setIsUserLoggedIn(false)
        view.openLoginActivity()
    }

    fun onChangeGroupClick(groupName: String) {

        for (group in mRepo.appRepo.groups){
            if(group.groupName == groupName){
                mRepo.prefs.setCurrentGroupName(groupName)
                mRepo.prefs.setCurrentGroupId(group.id)
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
        view.showCreatePingView()
    }

    override fun onDetach() {
        super.onDetach()
       // mRepo.locationProvider.end()
    }

    override fun onMapPrepared() {
        compositeDisposable.add(mRepo.getPings()
                .subscribe({ping: Ping? ->
                    Log.d("pings","asdasd")
                    if (ping != null)
                        view.updatePings(ping)
                },{t: Throwable? ->
                    Log.d("error",t!!.message)
                }))
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
        view.openHistoryFragment()
    }

    override fun clearData() {
        mRepo.clearData()
    }

}