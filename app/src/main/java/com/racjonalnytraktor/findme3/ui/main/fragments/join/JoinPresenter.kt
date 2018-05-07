package com.racjonalnytraktor.findme3.ui.main.fragments.join

import android.util.Log
import com.racjonalnytraktor.findme3.data.repository.join.JoinRepositoryImpl
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.SchedulerProvider

class JoinPresenter<V: JoinMvp.View>: BasePresenter<V>(),JoinMvp.Presenter<V> {

    val repo = JoinRepositoryImpl()

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        compositeDisposable.add(repo.getInvitations()
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe {item ->
                    view.updateList(item)
                })
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.dispose()
    }
}