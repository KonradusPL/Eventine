package com.racjonalnytraktor.findme3.data.local.model

import com.racjonalnytraktor.findme3.data.model.User
import io.realm.RealmObject

open class UserRealm: RealmObject(){
    var token: String = ""
    var fullName: String = ""
    var email: String = ""
    var isUserLoggedIn: Boolean = false
    var profileImage = ""
    var facebookId = ""
    var eventId = ""
    var eventName = ""
    var role = ""
    var isPartner: Boolean = false

    fun getUser(): User{
        return  User(facebookId,profileImage,fullName,token,"",email)
    }

    fun setUser(user: User){
        token = user.token
        fullName = user.fullName
        email = user.email
        facebookId = user.facebookId
        profileImage = user.profileUri
    }
}