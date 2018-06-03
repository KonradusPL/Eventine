package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable

object HistoryRepository: BaseRepository() {
    fun getPings(): Observable<Ping> {
        return rest.networkService.getPings(prefs.getUserToken(),prefs.getCurrentGroup())
                .map { t -> t.pings }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getInfos(): Observable<Info>{
        return rest.networkService.getInfos(prefs.getUserToken(),prefs.getCurrentGroup())
                .map { t -> t.info}
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}