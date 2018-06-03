package com.racjonalnytraktor.findme3.ui.map

import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface MapMvp {
    interface View: MvpView{
        fun changeCreateGroupFragment()
        fun updateSubGroups(item: String)
        fun showCreatePingView(type: String = "ping")
        fun hideCreatePingView()
        fun updatePings(ping: Ping)
        fun getPresenter(): MapPresenter<View>
        fun updateWithSavedData(task: String, descr: String, checked: List<String>, type: String,state: String)
        fun updateCheckedGroups(checked: List<String>)
        fun openManageActivity()
        fun openHistoryFragment()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onNextButtonClick(task: String, descr: String)
        fun onAddButtonClick(checkedGroups: ArrayList<String>)
        fun onMapLongClick(location: LatLng)
        fun onInfoTabClick()
        fun onMapPrepared()
        fun onSavingState(checked: List<String>, task: String, descr: String,state: String)
        fun onHistoryButtonClick()
        fun clearData()
    }
}