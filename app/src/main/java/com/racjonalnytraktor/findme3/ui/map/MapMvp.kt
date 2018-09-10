package com.racjonalnytraktor.findme3.ui.map

import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface MapMvp {
    interface View: MvpView{
        fun changeCreateGroupFragment()
        fun updateSubGroups(item: String)
        fun animateTabLayout()
        fun showCreatePingView(type: String = "ping")
        fun hideCreatePingView()
        fun updatePings(pings: List<Ping>,value: Boolean = true)
        fun addPing(ping: Ping)
        fun getPresenter(): MapPresenter<View>
        fun updateWithSavedData(task: String, descr: String, checked: List<String>, type: String,state: String)
        fun updateCheckedGroups(checked: List<String>)
        fun showEndPingBar(ping: Ping)
        fun showPlanDialog()
        fun removePing(pingId: String)
        fun clearPings()
        fun openManageActivity()
        fun openLoginActivity()
        fun openMapActivity()
        fun animateExtendedCircle(show: Boolean)
        fun showSlide(type: String)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        //onClick:
        fun onCircleClick(visibility: Int)
        fun onOrganiserClick()
        fun onHelpClick()
        fun onNextButtonClick(task: String, descr: String)
        fun onAddButtonClick(checkedGroups: ArrayList<String> = ArrayList(), date: String = "")
        fun onPlanButtonClick(checkedGroups: ArrayList<String>)
        fun onMapLongClick(location: LatLng)
        fun onInfoTabClick()
        fun onMapPrepared()
        fun onSavingState(checked: List<String>, task: String, descr: String,state: String)
        fun onHistoryButtonClick()
        fun clearData()
        fun onLogoutButtonClick()
        fun onEndPing(id: String)
        fun onInProgressClick(id: String)
    }
}