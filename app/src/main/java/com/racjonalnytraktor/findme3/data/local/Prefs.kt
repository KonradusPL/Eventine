package com.racjonalnytraktor.findme3.data.local

import com.racjonalnytraktor.findme3.data.model.User

interface Prefs {
    fun getUserToken(): String
    fun setUserToken(token: String)

    fun setUserFullName(fullName: String)
    fun getUserFullName(): String

    fun setUserEmail(email: String)
    fun getUserEmail(): String

    fun isUserLoggedIn(): Boolean
    fun setIsUserLoggedIn(value: Boolean)

    fun setUserProfileImage(value: String)
    fun getUserProfileImage(): String

    fun getCurrentUser(): User
    fun setCurrentUser(value: User)

    fun setFacebookId(value: String)
    fun getFacebookId(): String

    fun setCurrentGroup(value: String)
    fun getCurrentGroup(): String

}