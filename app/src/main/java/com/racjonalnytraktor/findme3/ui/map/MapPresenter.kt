package com.racjonalnytraktor.findme3.ui.map

import android.Manifest
import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.PermissionsHelper

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V> {

    lateinit var mRepo: MapRepository

    var typeOfNewThing = "ping"

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        mRepo = MapRepository(mvpView as Context)
        mRepo.onAttatch(mvpView.getCtx())
        view.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { state: PermissionsHelper.PermissionState? ->
                    if (state == PermissionsHelper.PermissionState.GRANTED)
                        view.checkLocationSettings()
                                .subscribe {
                                    mRepo.locationProvider.start()
                                }
                }
    }

    override fun onNextButtonClick(task: String, descr: String) {
        view.changeCreateGroupFragment()
        if(typeOfNewThing == "ping"){
            mRepo.newPing.title = task
            mRepo.newPing.desc = descr

        }
        else
            mRepo.newInfo.content = descr

        mRepo.getAllSubGroups()
                .flatMapIterable { t -> t }
                .subscribe({t: String? ->
                    Log.d("asdasd",t!!.toString())
                    view.updateSubGroups(t)
                },{t: Throwable? ->
                    Log.d("asdasd",t?.message.orEmpty())
                })

    }

    override fun onAddButtonClick(checkedGroups: ArrayList<String>) {


        if(typeOfNewThing == "ping"){
            mRepo.newPing.targetGroups = checkedGroups
            compositeDisposable.add(mRepo.createPing()
                    .subscribe({t: String? ->
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

    override fun onMapLongClick(location: LatLng) {
        typeOfNewThing = "ping"
        mRepo.newPing.geo.add(location.latitude)
        mRepo.newPing.geo.add(location.longitude)
        view.showCreatePingView()
    }

    override fun onDetach() {
        super.onDetach()
        mRepo.locationProvider.end()
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

    override fun onInfoTabClick() {
        typeOfNewThing = "info"
        view.showCreatePingView("info")
    }

}