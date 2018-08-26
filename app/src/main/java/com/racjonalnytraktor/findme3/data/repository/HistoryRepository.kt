package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable

object HistoryRepository: BaseRepository() {
    fun getPings(): Observable<Ping> {
        val array = ArrayList<Ping>()
        array.add(Ping(pingId = "1",title = "Go to kitchen",geo = arrayListOf(51.101850,22.853889),desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."))
        array.add(Ping(pingId = "2",title = "Please, remove rubbish",geo = arrayListOf(51.101628,22.853626),desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."))
        array.add(Ping(pingId = "3",title = "meeting in the dining room",geo = arrayListOf(51.101599,22.854527),desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."))
        return Observable.just(array).flatMapIterable { t -> t }

        return rest.networkService.getPings(prefs.getUserToken(),prefs.getCurrentGroupId())
                .map { t -> t.pings }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getInfos(): Observable<Info>{
        return rest.networkService.getInfos(prefs.getUserToken(),prefs.getCurrentGroupId())
                .map { t -> t.info}
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}