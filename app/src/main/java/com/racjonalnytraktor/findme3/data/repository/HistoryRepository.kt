package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable

object HistoryRepository: BaseRepository() {
    fun getPings(): Observable<Ping> {
        return MapRepository.rest.networkService.getPings(MapRepository.prefs.getUserToken(), MapRepository.prefs.getCurrentGroup())
                .map { t -> t.pings }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}