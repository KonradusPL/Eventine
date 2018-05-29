package com.racjonalnytraktor.findme3.ui.main

import com.racjonalnytraktor.findme3.data.repository.main.MainRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter

class MainPresenter<V: MainMvp.View>: BasePresenter<V>(),MvpPresenter<V> {

    val repo = MainRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView.getCtx())

        view.changeProfileIcon(repo.prefs.getUserProfileImage())
    }

}