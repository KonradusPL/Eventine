package com.racjonalnytraktor.findme3.ui.login

import com.facebook.login.LoginResult
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface LoginMvp {
    interface View: MvpView{
        fun openMainActivity()
        fun openRegisterActivity()
        fun showLoginLoading()
        fun hideLoginLoading()

    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onEmailLoginClick(email: String, password: String)
        fun onFacebookLoginClick()
        fun onGoogleLoginClick()
        fun onFacebookLoginSuccess(loginResult: LoginResult?)
    }
}