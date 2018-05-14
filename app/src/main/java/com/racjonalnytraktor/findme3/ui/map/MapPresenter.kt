package com.racjonalnytraktor.findme3.ui.map

import android.content.Context
import com.racjonalnytraktor.findme3.data.repository.map.MapRepositoryImpl
import com.racjonalnytraktor.findme3.ui.base.BasePresenter

class MapPresenter<V: MapMvp.View>: BasePresenter<V>(),MapMvp.Presenter<V> {

    lateinit var mRepo: MapRepositoryImpl

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        mRepo = MapRepositoryImpl(mvpView as Context)
        mRepo.locationProvider.init()
    }
}