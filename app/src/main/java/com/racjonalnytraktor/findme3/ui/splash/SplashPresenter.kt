package com.racjonalnytraktor.findme3.ui.splash

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.repository.SplashRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter

class SplashPresenter<V: SplashMvp.View>: BasePresenter<V>(),SplashMvp.Presenter<V> {

    lateinit var repo: SplashRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo = SplashRepository()

        if(repo.isUserLoggedIn()){
            if (repo.isUserInGroup())
                view.openMapActivity()
            else
                view.openMainActivity()

        }
        else
            view.openLoginActivity()
    }
}