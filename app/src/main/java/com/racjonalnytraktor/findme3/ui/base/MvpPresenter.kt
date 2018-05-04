package com.racjonalnytraktor.findme3.ui.base

interface MvpPresenter<in V: MvpView> {
    fun onAttach(mvpView: V)
    fun onDetach()
}