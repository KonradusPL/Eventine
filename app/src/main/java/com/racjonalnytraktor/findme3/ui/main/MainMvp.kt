package com.racjonalnytraktor.findme3.ui.main

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface MainMvp {
    interface View: MvpView{

    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}