package com.racjonalnytraktor.findme3.data.model

import com.facebook.AccessToken
import com.google.gson.annotations.SerializedName

class User(
    var facebookId: String = "",
    var profileUri: String = "",
    var fullName: String = "",
    var token: String = "",
    @SerializedName("_id")var id: String = "")