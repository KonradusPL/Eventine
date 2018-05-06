package com.racjonalnytraktor.findme3.ui.login

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface LoginMvp {
    interface View: MvpView{
        fun openMainActivity()
        fun openRegisterActivity()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onEmailLoginClick(email: String, password: String)
        fun onFacebookLoginClick()
        fun onGoogleLoginClick()
    }
}