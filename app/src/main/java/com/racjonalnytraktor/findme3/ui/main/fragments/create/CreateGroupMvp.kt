package com.racjonalnytraktor.findme3.ui.main.fragments.create

import com.racjonalnytraktor.findme3.data.model.Person
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface CreateGroupMvp {
    interface View: MvpView{
        fun updateList(person: Person)
    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}