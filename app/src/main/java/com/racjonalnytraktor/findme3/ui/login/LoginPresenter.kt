package com.racjonalnytraktor.findme3.ui.login

import android.content.Context
import android.util.Log
import com.facebook.login.LoginResult
import com.racjonalnytraktor.findme3.data.model.UserFacebook
import com.racjonalnytraktor.findme3.data.network.model.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.LoginResponse
import com.racjonalnytraktor.findme3.data.repository.LoginRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.StringHelper

class LoginPresenter<V: LoginMvp.View>: BasePresenter<V>(), LoginMvp.Presenter<V> {

    val repo = LoginRepository()

    override fun onEmailLoginClick(email: String, password: String) {
        compositeDisposable.add(repo.loginWithEmail(LoginRequest(email,password))
                .subscribe({response: LoginResponse? ->

                },{throwable: Throwable? ->
                    val errorCode = StringHelper.getErrorCode(throwable!!.localizedMessage)
                    view.showMessage(errorCode)
                }))
    }

    override fun onFacebookLoginClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoogleLoginClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFacebookLoginSuccess(loginResult: LoginResult?) {
        repo.getUserBasicInfo()
                .subscribe({ userFacebook: UserFacebook? ->
                    Log.d("userFacebook",userFacebook.toString())
                },{throwable: Throwable? ->
                    Log.d("error",throwable.toString())
                })
    }

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView as Context)
    }
}