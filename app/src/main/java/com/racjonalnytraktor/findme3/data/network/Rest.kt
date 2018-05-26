package com.racjonalnytraktor.findme3.data.network

interface Rest {
    fun loginWithEmail(email: String, password: String)
    fun registerWithEmail(email: String, password: String)

}