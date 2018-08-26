package com.racjonalnytraktor.findme3.ui.login

import android.util.Log
import com.facebook.login.LoginResult
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbResponse
import com.racjonalnytraktor.findme3.data.repository.LoginRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.StringHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginPresenter<V: LoginMvp.View>: BasePresenter<V>(), LoginMvp.Presenter<V> {

    val repo = LoginRepository()

    override fun onEmailLoginClick(email: String, password: String) {
        view.showLoginLoading()
        doAsync {
            Thread.sleep(2000)
            uiThread {
                view.hideLoginLoading()
                view.openMainActivity()
            }
        }
        return
        compositeDisposable.add(repo.loginWithEmail(LoginRequest(email, password))
                .subscribe({response: LoginResponse? ->
                    view.hideLoginLoading()
                    repo.prefs.setIsUserLoggedIn(true)
                    repo.prefs.setUserToken(response!!.token)
                    view.openMainActivity()
                    view.showMessage("Sukces!",MvpView.MessageType.SUCCESS)

                },{throwable: Throwable? ->
                    view.hideLoginLoading()
                    val errorCode = StringHelper.getErrorCode(throwable!!.localizedMessage)
                    if(errorCode == "401")
                        view.showMessage("Błędne dane logowania",MvpView.MessageType.ERROR)
                }))
    }

    override fun onFacebookLoginClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoogleLoginClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFacebookLoginSuccess(loginResult: LoginResult?) {
        view.showLoginLoading()
        repo.getUserInfo()
                .subscribe({ user: User? ->
                    Log.d("qweqwe",user!!.fullName)
                    repo.registerByFacebook(user!!)
                            .subscribe ({ response: RegisterFbResponse? ->
                                view.hideLoginLoading()
                                Log.d("registerresponse",response!!.token)
                                user.token = response.token
                                repo.prefs.setIsUserLoggedIn(true)
                                repo.setCurrentUser(user)
                                view.openMainActivity()},
                                    {error: Throwable? -> Log.d("error",error.toString())
                                        view.hideLoginLoading()
                            }

                    )
                },{throwable: Throwable? ->
                    view.hideLoginLoading()
                    Log.d("error",throwable.toString())
                })
    }


    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttach(view.getCtx())
    }
}