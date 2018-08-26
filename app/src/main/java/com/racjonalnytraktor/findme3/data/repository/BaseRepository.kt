package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.facebook.FacebookNetwork

open class BaseRepository {
    val rest = RetrofitRest()
    val mFacebook = FacebookNetwork()
    lateinit var prefs: SharedPrefs
    val appRepo = ApplicationRepository

    fun onAttach(context: Context){
        prefs = SharedPrefs(context)
    }

}