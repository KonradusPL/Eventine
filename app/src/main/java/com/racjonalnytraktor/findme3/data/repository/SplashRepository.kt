package com.racjonalnytraktor.findme3.data.repository

class SplashRepository(): BaseRepository() {

    fun isUserLoggedIn(): Boolean {
        return preferences.isUserLoggedIn()
    }



}
