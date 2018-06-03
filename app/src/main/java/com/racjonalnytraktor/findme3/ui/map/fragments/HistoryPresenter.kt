package com.racjonalnytraktor.findme3.ui.map.fragments

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.HistoryRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter

class HistoryPresenter<V: HistoryMvp.View>: BasePresenter<V>(),HistoryMvp.Presenter<V> {

    val repo = HistoryRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView.getCtx())
        compositeDisposable.add(repo.getPings()
                .subscribe({ping: Ping? ->
                    if (ping != null)
                        view.updatePings(ping)
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }

    override fun onInfoButtonClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPingButtonClick() {
        compositeDisposable.add(repo.getPings()
                .subscribe({ping: Ping? ->
                    if (ping != null)
                    view.updatePings(ping)
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }

    override fun onAllButtonClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}