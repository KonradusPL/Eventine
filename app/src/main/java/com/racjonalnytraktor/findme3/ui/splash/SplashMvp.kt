package com.racjonalnytraktor.findme3.ui.splash

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface SplashMvp {
    interface View: MvpView{
        fun openMainActivity()
        fun openMapActivity()
        fun openLoginActivity()
    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}