package com.racjonalnytraktor.findme3.ui.splash

import android.content.Context
import com.racjonalnytraktor.findme3.data.repository.SplashRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter

class SplashPresenter<V: SplashMvp.View>: BasePresenter<V>(),SplashMvp.Presenter<V> {

    lateinit var repo: SplashRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo = SplashRepository()
        repo.onAttatch(mvpView as Context)
        if(repo.isUserLoggedIn())
            view.openMainActivity()
        else
            view.openMainActivity()
    }
}