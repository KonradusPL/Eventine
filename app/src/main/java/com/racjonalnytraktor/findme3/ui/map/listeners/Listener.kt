package com.racjonalnytraktor.findme3.ui.map.listeners

import android.location.Location
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job

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
    interface Organisers{
        fun showList(list: ArrayList<Job>)
        fun showLoading()
        fun hideLoading()
    }
}