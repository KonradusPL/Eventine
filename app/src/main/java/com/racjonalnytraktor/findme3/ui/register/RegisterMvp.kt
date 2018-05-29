package com.racjonalnytraktor.findme3.ui.register

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface RegisterMvp {
    interface View: MvpView{
        fun openMainActivity()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onRegisterButtonClick(email: String, password: String, fullName: String)
    }
}