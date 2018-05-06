package com.racjonalnytraktor.findme3.ui.main.fragments.change

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface ChangeGroupMvp {
    interface View: MvpView{

    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}