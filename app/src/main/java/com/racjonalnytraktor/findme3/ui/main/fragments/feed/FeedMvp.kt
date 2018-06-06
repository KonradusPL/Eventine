package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.adapters.GroupsListAdapter
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface FeedMvp {
    interface View: MvpView,GroupsListAdapter.GroupsListListener{
        fun updateGroupsList(group: Group)
        fun updateTasksList(task: Ping)
        fun openMapActivity(groupName: String)
        fun showGroupsLoading()
        fun hideGroupsLoading()
        fun showTasksLoading()
        fun hideTasksLoading()
        fun updateTasks(ping: Ping)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onGroupItemClick(groupName: String, groupId: String)
    }
}