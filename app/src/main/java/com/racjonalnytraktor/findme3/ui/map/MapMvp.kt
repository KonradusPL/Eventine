package com.racjonalnytraktor.findme3.ui.map

import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.ui.map.listeners.Listener
import com.racjonalnytraktor.findme3.utils.MapHelper

interface MapMvp {
    interface View: MvpView,MapHelper.MapViewListener{
        fun updateSubGroups(item: String)
        fun animateTabLayout(show: Boolean)
        fun hideCreatePingView()
        fun updatePings(pings: List<Ping>,value: Boolean = true)
        fun updatePings(ping: Action)
        fun addPing(ping: Ping)
        fun getPresenter(): MapPresenter<View>
        fun updateWithSavedData(task: String, descr: String, checked: List<String>, type: String,state: String)
        fun updateCheckedGroups(checked: List<String>)
        fun removePing(pingId: String)
        fun clearPings()
        fun openManageActivity()
        fun openLoginActivity()
        fun openMapActivity()
        fun animateExtendedCircle(show: Boolean)
        fun showCreatePingView(type: String = "ping")
        fun showSlide(type: String)
        fun hideSlide()
        fun removeFragment(type: String)
        fun showEndPingBar(ping: Ping)
        fun showPlanDialog()
        fun showFullFragments(type: String)
        fun hideFullFragments(type: String, unSelectTab: Boolean = false)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        //clickers
        fun onCircleClick(visibility: Int)
        fun onOrganiserClick()
        fun onHelpClick()
        fun onNextButtonClick(task: String, descr: String)
        fun onAddButtonClick(checkedGroups: ArrayList<String> = ArrayList(), date: String = "")
        fun onPlanButtonClick(checkedGroups: ArrayList<String>)
        fun onMapLongClick(location: LatLng)
        fun onGroupsClick()
        fun onOptionsClick()
        fun onInfoTabClick()
        fun onProfileClick()
        fun onHistoryButtonClick()
        fun onLogoutButtonClick()
        fun onInProgressClick(id: String)
        fun onBackInFragmentClick(type: String)
        fun onChangeLocationClick(locationListener: Listener.ChangeLocation)
        fun onCreateActionClick(action: CreateActionRequest, listener: Listener.CreateAction)
        fun onLogOutClick()

        //others
        fun onSlideHide()
        fun onMapPrepared()
        fun onSavingState(checked: List<String>, task: String, descr: String,state: String)
        fun clearData()
        fun onEndPing(id: String)
        fun onManageGroupAttach(listener: Listener.Manage)
        fun onAddTaskListAttach(listener: Listener.AddTaskList)
        fun onOrganisersAttach(listener: Listener.Organisers)

    }
}