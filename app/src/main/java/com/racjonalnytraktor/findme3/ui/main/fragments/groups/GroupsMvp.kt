package com.racjonalnytraktor.findme3.ui.main.fragments.groups

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface GroupsMvp {
    interface View: MvpView{

    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}