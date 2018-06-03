package com.racjonalnytraktor.findme3.ui.main

import android.util.Log
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.iid.FirebaseInstanceId
import com.racjonalnytraktor.findme3.data.model.UpdateTokenRequest
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.main.MainRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.utils.SchedulerProvider

class MainPresenter<V: MainMvp.View>: BasePresenter<V>(),MainMvp.Presenter<V> {

    val repo = MainRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView.getCtx())

        view.changeProfileIcon(repo.prefs.getUserProfileImage())

        val token = FirebaseInstanceId.getInstance().token
        val request = UpdateTokenRequest(token!!)

        Log.d("header",repo.prefs.getUserToken())
        Log.d("token",token)
        compositeDisposable.add(repo.rest.networkService.updateNotifToken(repo.prefs.getUserToken(),request!!)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("tokenik",t)
                },{t: Throwable? ->
                    Log.d("tokenik",t!!.message)
                }))
    }

    override fun sendNotifToken(token: String) {

        val request = UpdateTokenRequest(token)

        repo.rest.networkService.updateNotifToken(repo.prefs.getUserToken(),request)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("tokenik",t)
                },{t: Throwable? ->
                    Log.d("tokenik",t!!.message)
                })
    }

    override fun onLogoutButtonClick() {
        if(AccessToken.isCurrentAccessTokenActive())
            LoginManager.getInstance().logOut()
        repo.prefs.setCurrentUser(User())
        repo.prefs.setIsUserLoggedIn(false)
        view.openLoginActivity()
    }

}