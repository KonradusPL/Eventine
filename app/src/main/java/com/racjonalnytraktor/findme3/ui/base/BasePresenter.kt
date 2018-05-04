package com.racjonalnytraktor.findme3.ui.base

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V: MvpView>: MvpPresenter<V> {

    lateinit var view: V
    val compositeDisposable = CompositeDisposable()

    override fun onAttach(mvpView: V) {
        view = mvpView
    }

    override fun onDetach() {
        compositeDisposable.clear()
    }
}