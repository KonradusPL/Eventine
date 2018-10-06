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

        val token1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6IkphbiBLb3dhbHNraSIsImlkIjoiNWJhYzkyN2QwYzUxZjMwMDEwZjVkMDhlIiwiaWF0IjoxNTM4MDM2MzQ5LCJleHAiOjE1Mzg2NDExNDl9.4K1c_fN50r8qgTpUanUw_p15oAJ9924lzChNzQ13U9o"
        val request2 = RegisterRequest("test1@test.pl", "Jan Kowalski", "password1")
        val grupaTestowa1 = "5bb206e3c4b7060010e4c667"

        if(repo.isUserLoggedIn())
            view.openMainActivity()
        else
            view.openLoginActivity()
    }
}