package com.racjonalnytraktor.findme3.data.model

import com.facebook.AccessToken
import com.google.gson.annotations.SerializedName

class User(
    var facebookId: String = "",
    var profileUri: String = "",
    var fullName: String = "",
    var token: String = "",
    @SerializedName("_id")var id: String = "",
    var email: String = "",
    var password: String = "",
    var isPartner: Boolean = false){

    override fun toString(): String {
        return "facebook: $facebookId \n " +
                "profileUri: $profileUri \n" +
                "fullName: $fullName \n" +
                "token: $token \n" +
                "email: $email"
    }
}