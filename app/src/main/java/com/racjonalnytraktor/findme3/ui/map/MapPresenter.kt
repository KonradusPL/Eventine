package com.racjonalnytraktor.findme3.ui.map

import android.Manifest
import android.content.Context
import android.util.Log
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.PermissionsHelper

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V> {

    lateinit var mRepo: MapRepository

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
        mRepo.task = task
        mRepo.descr = descr
        mRepo.getAllSubGroups()
                .flatMapIterable { t -> t }
                .subscribe({t: String? ->
                    Log.d("asdasd",t!!.toString())
                    view.updateSubGroups(t)
                },{t: Throwable? ->
                    Log.d("asdasd",t?.message.orEmpty())
                })
    }

    override fun onDetach() {
        super.onDetach()
        mRepo.locationProvider.end()
    }

}