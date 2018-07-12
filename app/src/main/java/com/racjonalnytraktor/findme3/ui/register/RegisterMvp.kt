package com.racjonalnytraktor.findme3.ui.register

import com.facebook.login.LoginResult
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface RegisterMvp {
    interface View: MvpView{
        fun openMainActivity()
        fun showLoginLoading()
        fun hideLoginLoading()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onRegisterButtonClick(email: String, password: String, fullName: String)
        fun onFacebookLoginSuccess(loginResult: LoginResult?)
    }
}