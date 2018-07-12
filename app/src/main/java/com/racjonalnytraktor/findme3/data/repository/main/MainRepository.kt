package com.racjonalnytraktor.findme3.data.repository.main

import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.BaseRepository

object MainRepository: BaseRepository() {

    fun getCurrentUser(): User{
        return prefs.getCurrentUser()
    }

}