package com.racjonalnytraktor.findme3.ui.main.fragments.join

import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface JoinMvp {
    interface View: MvpView{
        fun updateList(invitation: Invitation)
    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}