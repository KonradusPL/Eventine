package com.racjonalnytraktor.findme3.data.repository.splash

import android.content.Context
import android.content.SharedPreferences
import com.racjonalnytraktor.findme3.data.local.SharedPrefs

class SplashRepositoryImpl(val context: Context): SplashRepository {

    val prefs = SharedPrefs(context)

    override fun isUserLoggedIn(): Boolean {
        return prefs.isUserLoggedIn()
    }

}
