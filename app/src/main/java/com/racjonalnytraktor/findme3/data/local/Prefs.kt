package com.racjonalnytraktor.findme3.data.local

/**
 * Created by Admin on 2018-05-06.
 */
interface Prefs {
    fun getUserToken(): String
    fun setUserToken(token: String)
    fun setUserFullName(fullName: String)
    fun getUserFullName(): String
    fun setUserEmail(email: String)
    fun getUserEmail(): String
    fun isUserLoggedIn(): Boolean
    fun setIsUserLoggedIn(value: Boolean)
}