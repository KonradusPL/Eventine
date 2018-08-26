package com.racjonalnytraktor.findme3.ui.register

import android.util.Log
import com.facebook.login.LoginResult
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import com.racjonalnytraktor.findme3.data.repository.RegisterRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

class RegisterPresenter<V: RegisterMvp.View>: BasePresenter<V>(), RegisterMvp.Presenter<V> {

    val repo = RegisterRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttach(view.getCtx())
    }

    override fun onRegisterButtonClick(email: String, password: String, fullName: String) {
        val mathResult = email.length * password.length * fullName.length
        if(mathResult == 0){
            view.showMessage(R.string.text_fill_all_fields,MvpView.MessageType.INFO)
            return
        }

        if(!view.isConnectedToNetwork()) {
            view.showMessage(R.string.text_turn_on_internet_connection,MvpView.MessageType.INFO)
            return
        }

        view.showLoading()

        val request = RegisterRequest(email, fullName, password)
        compositeDisposable.add(repo.registerUser(request)
                .subscribe({response: RegisterResponse? ->
                    view.hideLoading()
                    Log.d("token",response!!.token)
                    val user = User("","",fullName,response.token)
                    repo.prefs.setCurrentUser(user)
                    repo.prefs.setIsUserLoggedIn(true)
                    view.showMessage("Udało się !",MvpView.MessageType.SUCCESS)
                    view.openMainActivity()
                },{t: Throwable? ->
                    view.hideLoading()
                    view.showMessage("Uzupełnij poprawnie pola",MvpView.MessageType.INFO)
                }))
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

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
}