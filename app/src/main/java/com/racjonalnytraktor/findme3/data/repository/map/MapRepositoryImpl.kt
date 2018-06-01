package com.racjonalnytraktor.findme3.data.repository.map

import android.app.Activity
import android.content.Context
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.utils.LocationProvider


class MapRepositoryImpl(context: Context) {

    var task: String? = null
    var descr: String? = null

    val locationProvider = LocationProvider(1000,context)

}