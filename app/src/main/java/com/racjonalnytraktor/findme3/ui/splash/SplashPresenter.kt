package com.racjonalnytraktor.findme3.ui.splash

import android.content.Context
import android.os.CpuUsageInfo
import com.racjonalnytraktor.findme3.data.repository.splash.SplashRepositoryImpl
import com.racjonalnytraktor.findme3.data.repository.splash.SplashRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter

class SplashPresenter<V: SplashMvp.View>: BasePresenter<V>(),SplashMvp.Presenter<V> {

    lateinit var repo: SplashRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo = SplashRepositoryImpl(mvpView as Context)
        if(repo.isUserLoggedIn())
            view.openMainActivity()
        else
            view.openLoginActivity()
    }
}