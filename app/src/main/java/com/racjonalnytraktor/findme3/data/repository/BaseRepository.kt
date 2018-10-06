package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import com.facebook.share.Share
import com.racjonalnytraktor.findme3.data.local.Prefs
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.data.local.db.RealmLocalDb
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.facebook.FacebookNetwork

open class BaseRepository {
    val rest = RetrofitRest()
    val facebook = FacebookNetwork()
    val appRepo = ApplicationRepository
    val prefs: Prefs = RealmLocalDb()

    fun saveUser(token: String,facebookId: String,email: String){
        val user = User(facebookId,"","",token,"",email)
        prefs.setCurrentUser(user)
        prefs.setCurrentGroupId("5bb206e3c4b7060010e4c667")
        prefs.setIsUserLoggedIn(true)
    }
}