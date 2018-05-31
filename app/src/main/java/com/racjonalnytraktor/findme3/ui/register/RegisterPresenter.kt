package com.racjonalnytraktor.findme3.ui.register

import android.util.Log
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import com.racjonalnytraktor.findme3.data.repository.RegisterRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.StringHelper

class RegisterPresenter<V: RegisterMvp.View>: BasePresenter<V>(), RegisterMvp.Presenter<V> {

    val repo = RegisterRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView.getCtx())
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
                    view.showMessage(R.string.text_success,MvpView.MessageType.SUCCESS)
                    view.openMainActivity()
                },{t: Throwable? ->
                    view.hideLoading()
                    view.showMessage(StringHelper.getErrorCode(t?.message.orEmpty()),MvpView.MessageType.ERROR)
                }))
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }

}