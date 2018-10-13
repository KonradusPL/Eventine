package com.racjonalnytraktor.findme3.ui.map.listeners

import android.location.Location
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job

object Listener {
    interface Manage{
        fun showList(list: List<Job>)
        fun showLoading()
        fun hideLoading()
        fun onError()
    }
    interface AddTaskList{
        fun showListLoading()
        fun hideListLoading()
        fun showError()
        fun showList(list: ArrayList<Job>)
    }
    interface ChangeLocation{
        fun changeLocation(location: Location)
    }
    interface Organisers{
        fun showList(list: ArrayList<Job>)
        fun showLoading()
        fun hideLoading()
    }
    interface ChangeDate{
        fun onDateChanged()
    }
    interface CreateAction{
        fun clearData()
    }
}