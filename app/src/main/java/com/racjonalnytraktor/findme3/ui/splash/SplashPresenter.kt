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

        val token2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6Ik1hcmNpbiBLb3dhbHNraSIsImlkIjoiNWJhYzkyYjUwYzUxZjMwMDEwZjVkMDhmIiwiaWF0IjoxNTM4MDM2NDA1LCJleHAiOjE1Mzg2NDEyMDV9.i4_JXB9iREQlJ7ioPWvf4algZSaJLxzpj6PZOJygf7Y"
        val request2 = RegisterRequest("test2@test.pl", "Marcin Kowalski", "password2")
        val grupaTestowa1 = "5bb206e3c4b7060010e4c667"
        repo.prefs.apply {
            createUser(User())
            setUserToken(token2)
            setUserFullName(request2.fullName)
            setUserEmail(request2.email)
            setCurrentGroupId(grupaTestowa1)
            setIsUserLoggedIn(true)
        }
        Log.d("userqwe",repo.prefs.isUserLoggedIn().toString())

        if(repo.isUserLoggedIn())
            view.openMainActivity()
        else
            view.openLoginActivity()
    }
}