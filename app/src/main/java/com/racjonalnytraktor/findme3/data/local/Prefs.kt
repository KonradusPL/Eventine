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

    fun setCurrentGroupId(value: String)
    fun getCurrentGroupId(): String

    fun setCurrentGroupName(value: String)
    fun getCurrentGroupName(): String

    fun setRole(value: String)
    fun getRole(): String

    fun setIsPartner(value: Boolean)
    fun isPartner(): Boolean

    fun createUser(user: User)
    fun removeUser()

    fun setIsSilentNotification(value: Boolean)
    fun isSilentNotification(): Boolean

}