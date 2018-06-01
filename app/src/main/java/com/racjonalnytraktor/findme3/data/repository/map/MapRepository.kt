package com.racjonalnytraktor.findme3.data.repository.map

import android.content.Context
import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.LocationProvider
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import org.jetbrains.anko.doAsync


class MapRepository(context: Context): BaseRepository() {

    val newPing = Ping()
    val locationProvider = LocationProvider(1000,context)
    init {
        doAsync {
            Log.d("lulala",newPing.title+ " ")
        }
    }

    val pings = ArrayList<Ping>()

    fun getAllSubGroups(): Observable<List<String>>{
        Log.d("zzzzzz",prefs.getUserToken())
        Log.d("zzzzzz",prefs.getCurrentGroup())
        return rest.networkService.getAllSubGroups(prefs.getUserToken(),prefs.getCurrentGroup())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun createPing(): Single<String>{
        Log.d("opopop",newPing.title)
        Log.d("request","${newPing.desc} ${newPing.title} ${newPing.geo} " +
                "${newPing.groupId} ${newPing.targetGroups} ")
        newPing.groupId = prefs.getCurrentGroup()
        Log.d("title",newPing.title)
        Log.d("desc",newPing.desc)
        Log.d("targetGroups",newPing.targetGroups.toString())
        Log.d("groupId",newPing.groupId)
        Log.d("geo",newPing.geo.toString())

        return rest.networkService.createPing(prefs.getUserToken(),newPing)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())

    }

    fun getPings(): Observable<Ping>{
        return rest.networkService.getPings(prefs.getUserToken(),prefs.getCurrentGroup())
                .map { t -> t.pings }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

}