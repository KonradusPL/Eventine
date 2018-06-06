package com.racjonalnytraktor.findme3.ui.map.fragments

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.data.repository.HistoryRepository
import com.racjonalnytraktor.findme3.ui.adapters.HistoryAdapter
import com.racjonalnytraktor.findme3.ui.base.BasePresenter

class HistoryPresenter<V: HistoryMvp.View>: BasePresenter<V>(),HistoryMvp.Presenter<V>
,HistoryAdapter.ClickListener{

    val repo = HistoryRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        repo.onAttatch(mvpView.getCtx())

        compositeDisposable.add(repo.getPings()
                .subscribe({ping: Ping? ->
                    Log.d("pongaponga","pingapinga")
                    if (ping != null){
                        Log.d("pongaponga","pongaponga")
                        ping.type = "ping"
                        view.updatePings(ping)
                    }
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }

    override fun onInfoButtonClick() {
        view.clearList("info")
        compositeDisposable.add(repo.getInfos()
                .subscribe({info: Info? ->
                    if (info != null){
                        info.type = "info"
                        view.updateInfos(info)
                    }
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }

    override fun onPingButtonClick() {
        view.clearList("pings")
        compositeDisposable.add(repo.getPings()
                .subscribe({ping: Ping? ->
                    if (ping != null){
                        ping.type = "ping"
                        view.updatePings(ping)
                    }
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }

    override fun onInfoClick(info: Info) {
        view.showEndPingBar(info)
    }

    override fun onPingClick(ping: Ping) {
        view.showEndPingBar(ping)
    }

    override fun onAllButtonClick() {
        view.clearList("ping")
        compositeDisposable.add(repo.getPings()
                .subscribe({ping: Ping? ->
                    if (ping != null){
                        ping.type = "ping"
                        view.updatePings(ping)
                    }
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
        compositeDisposable.add(repo.getInfos()
                .subscribe({info: Info? ->
                    if (info != null){
                        info.type = "info"
                        view.updateInfos(info)
                    }
                },{t: Throwable? ->
                    Log.d("error",t.toString())
                }))
    }

    override fun onDetach() {
        super.onDetach()
    }

}