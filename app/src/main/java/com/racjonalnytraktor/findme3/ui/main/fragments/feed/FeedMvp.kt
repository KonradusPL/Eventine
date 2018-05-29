package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.ui.adapters.GroupsListAdapter
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface FeedMvp {
    interface View: MvpView,GroupsListAdapter.GroupsListListener{
        fun updateGroupsList(group: Group)
        fun updateTasksList(task: Task)
        fun openMapActivity(groupName: String)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onGroupItemClick(groupName: String)
    }
}