package com.racjonalnytraktor.findme3.ui.main.fragments.groups

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface GroupsMvp {
    interface View: MvpView{
        fun updateGroupsList(group: Group)
        fun updateTasksList(task: Task)
    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}