package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.map.ZoneUpdate
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
        fun updatePings(pings: List<Ping>,floor: Int)
        fun updatePings(ping: Action)
        fun updateZones(zones: ArrayList<ZoneUpdate>)
        fun addPing(ping: Ping)
        fun getPresenter(): MapPresenter<View>
        fun updateCheckedGroups(checked: List<String>)
        fun removePing(pingId: String)
        fun clearPings()
        fun openManageActivity()
        fun openLoginActivity()
        fun openMapActivity()
        fun openMainActivity()
        fun animateExtendedCircle(show: Boolean)
        fun showSlide(type: String, location: Location = Location(""))
        fun hideSlide()
        fun removeFragment(type: String)
        fun showEndPingBar(ping: Ping)
        fun showPlanDialog()
        fun showFullFragments(type: String)
        fun hideFullFragments(type: String, unSelectTab: Boolean = false)
        fun hideFullFragments()
        fun changeBeaconsStatus(enable: Boolean)
        fun showLoading()
        fun hideLoading()
        fun clearTab(position: Int, auto: Boolean = false)
        fun updateOnPartner()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        //clickers
        fun onFloorSelected(floor: Int)
        fun onCircleClick(visibility: Int)
        fun onOrganiserButtonClick()
        fun onOrganizerClick(organizerId: String)
        fun onHelpClick()
        fun onNextButtonClick(task: String, descr: String)
        fun onPlanButtonClick(checkedGroups: ArrayList<String>)
        fun onMapLongClick(location: LatLng)
        fun onGroupsClick()
        fun onOptionsClick()
        fun onInfoTabClick()
        fun onProfileClick()
        fun onHistoryButtonClick()
        fun onLogoutButtonClick()
        fun onInProgressClick(id: String)
        fun onEndPingClick(id: String)
        fun onBackInFragmentClick(type: String)
        fun onChangeLocationClick(locationListener: Listener.ChangeLocation)
        fun onCreateActionClick(action: CreateActionRequest, listener: Listener.CreateAction)
        fun onLogOutClick()
        fun onSilentSwitch(value: Boolean)
        fun onBackToMenuClick()

        //others
        fun onSlideHide()
        fun onMapPrepared()
        fun onSavingState(checked: List<String>, task: String, descr: String,state: String)
        fun clearData()
        fun onManageGroupAttach(listener: Listener.Manage)
        fun onAddTaskListAttach(listener: Listener.AddTaskList)
        fun onOrganisersAttach(listener: Listener.Organisers)
        fun onZoneEnter(zone: String)

    }
}