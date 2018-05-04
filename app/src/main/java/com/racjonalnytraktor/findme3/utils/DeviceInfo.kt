package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.net.ConnectivityManager

class DeviceInfo(context: Context) {

    var connection = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        val activeNetwork = connection.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}