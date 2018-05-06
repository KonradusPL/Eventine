package com.racjonalnytraktor.findme3.ui.base

import android.content.Context
import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V: MvpView>: MvpPresenter<V> {

    protected lateinit var view: V
    protected lateinit var context: Context
    val compositeDisposable = CompositeDisposable()

    override fun onAttach(mvpView: V) {
        view = mvpView
        context = mvpView as Context
    }

    override fun onDetach() {
        compositeDisposable.clear()
    }
}