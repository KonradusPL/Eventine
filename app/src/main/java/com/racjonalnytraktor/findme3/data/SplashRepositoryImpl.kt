package com.racjonalnytraktor.findme3.data

import android.content.Context
import com.racjonalnytraktor.findme3.data.repository.SplashRepository

class SplashRepositoryImpl(val context: Context): SplashRepository{

    override fun isUserLoggedIn(): Boolean {
        return false
    }

}
