package com.racjonalnytraktor.findme3.ui.map

import android.Manifest
import android.content.Context
import com.racjonalnytraktor.findme3.data.repository.map.MapRepositoryImpl
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.PermissionsHelper

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V> {

    lateinit var mRepo: MapRepositoryImpl

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        mRepo = MapRepositoryImpl(mvpView as Context)
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
    }

    override fun onDetach() {
        super.onDetach()
        mRepo.locationProvider.end()
    }
}