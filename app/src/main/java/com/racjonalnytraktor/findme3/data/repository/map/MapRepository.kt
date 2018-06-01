package com.racjonalnytraktor.findme3.data.repository.map

import android.app.Activity
import android.content.Context
import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.LocationProvider
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single


class MapRepository(context: Context): BaseRepository() {

    var task: String? = null
    var descr: String? = null
    val locationProvider = LocationProvider(1000,context)

    fun getAllSubGroups(): Observable<List<String>>{
        Log.d("zzzzzz",prefs.getUserToken())
        Log.d("zzzzzz",prefs.getCurrentGroup())
        return rest.networkService.getAllSubGroups(prefs.getUserToken(),prefs.getCurrentGroup())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

}