package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.Help
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.utils.ClassTransform
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

object HistoryRepository: BaseRepository() {

    val actions = ArrayList<Model1>()
    val listHelp = ArrayList<Model1>()

    fun getHelps(): Single<ArrayList<Help>> {
        return rest.networkService.getHelps(prefs.getUserToken())
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

    fun getActions(): Observable<List<Action>>{
        return rest.networkService.getActions(prefs.getUserToken(),prefs.getCurrentGroupId(),"ping")
                .map { t -> t.actions.reversed() }
                .toObservable()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}