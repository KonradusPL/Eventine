package com.racjonalnytraktor.findme3.ui.manage

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.repository.ManageRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter


class ManagePresenter<V: ManangeMvp.View>: BasePresenter<V>(),ManangeMvp.Presenter<V> {

    val repo = ManageRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView.getCtx())
        compositeDisposable.add(repo.getPeopleInGroups()
                .subscribe({t: Typed? ->
                    view.updateList(t!!)
                    Log.d("typed",t!!.type)
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }
}