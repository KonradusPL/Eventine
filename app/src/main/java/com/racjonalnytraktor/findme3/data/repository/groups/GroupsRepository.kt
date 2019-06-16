package com.racjonalnytraktor.findme3.data.repository.groups

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.*
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.ApplicationRepository.groups
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

object GroupsRepository: BaseRepository() {

    fun getTasks(): Observable<Ping> {
     /*return rest.networkService.getTasks(prefs.getUserToken())
             .map { t -> t.pings }
             .flatMapIterable { t -> t }
             .subscribeOn(SchedulerProvider.io())
             .observeOn(SchedulerProvider.ui())*/

        val arrayList = ArrayList<Ping>()
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        return Observable.just(arrayList)
                .flatMapIterable { t -> t }
                .delay(4L, TimeUnit.SECONDS)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())


    }

    fun getGroups(): Single<List<Group>> {
        return rest.networkService.getGroupList(prefs.getUserToken())
                .map { t: GroupsEntity -> t.groups }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}