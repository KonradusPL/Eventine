package com.racjonalnytraktor.findme3.ui.map.fragments

import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface HistoryMvp {
    interface View: MvpView{
        fun updateActions(action: ArrayList<Model1>)
        fun updateActions(action: Model1)
        //fun updateHelps(info: Info)
        fun updateAll()
        fun clearList(type: String)
        fun showEndPingBar(ping: Ping)
        fun showProgress()
        fun hideProgress()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onActionsButtonClick()
        fun onHelpButtonClick()
        fun onAllButtonClick()
    }
}