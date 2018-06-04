package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.facebook.FacebookNetwork

open class BaseRepository {
    val rest = RetrofitRest()
    lateinit var prefs: SharedPrefs
    val mFacebook = FacebookNetwork()

    val appRepo = ApplicationRepository

    fun onAttatch(context: Context){
        prefs = SharedPrefs(context)
    }



}