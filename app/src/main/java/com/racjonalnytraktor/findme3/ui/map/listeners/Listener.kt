package com.racjonalnytraktor.findme3.ui.map.listeners

import android.location.Location

object Listener {
    interface Manage{
        fun showList()
        fun showLoading()
        fun hideLoading()
    }
    interface AddTaskList{
        fun showListLoading()
        fun hideListLoading()
    }
    interface ChangeLocation{
        fun changeLocation(location: Location)
    }
}