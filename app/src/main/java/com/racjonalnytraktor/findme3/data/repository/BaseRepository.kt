package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import com.facebook.share.Share
import com.racjonalnytraktor.findme3.data.local.Prefs
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.data.local.db.RealmLocalDb
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.facebook.FacebookNetwork

open class BaseRepository {
    val rest = RetrofitRest()
    val mFacebook = FacebookNetwork()
    val appRepo = ApplicationRepository
    val prefs: Prefs = RealmLocalDb()
}