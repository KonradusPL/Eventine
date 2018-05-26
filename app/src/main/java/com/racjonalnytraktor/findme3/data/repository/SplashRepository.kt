package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.racjonalnytraktor.findme3.data.local.SharedPrefs

class SplashRepository(val context: Context): BaseRepository() {

    val prefs = SharedPrefs(context)

    fun isUserLoggedIn(): Boolean {
        return prefs.isUserLoggedIn()
    }



}
