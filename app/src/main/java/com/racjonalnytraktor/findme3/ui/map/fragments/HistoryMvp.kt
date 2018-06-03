package com.racjonalnytraktor.findme3.ui.map.fragments

import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface HistoryMvp {
    interface View: MvpView{
        fun updatePings(ping: Ping)
        fun updateInfos(info: Info)
        fun updateAll()
        fun clearList(type: String)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onInfoButtonClick()
        fun onPingButtonClick()
        fun onAllButtonClick()
    }
}