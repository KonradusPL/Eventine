package com.racjonalnytraktor.findme3.ui.map

import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface MapMvp {
    interface View: MvpView{
        fun changeCreateGroupFragment()
        fun updateSubGroups(item: String)
        fun showCreatePingView()
        fun hideCreatePingView()
        fun updatePings(ping: Ping)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onNextButtonClick(task: String, descr: String)
        fun onAddButtonClick(checkedGroups: ArrayList<String>)
        fun onMapLongClick(location: LatLng)
    }
}