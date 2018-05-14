package com.racjonalnytraktor.findme3.data.repository.map

import android.app.Activity
import android.content.Context
import com.racjonalnytraktor.findme3.utils.LocationProvider


class MapRepositoryImpl(context: Context) {
    val locationProvider = LocationProvider(1000,context)
}