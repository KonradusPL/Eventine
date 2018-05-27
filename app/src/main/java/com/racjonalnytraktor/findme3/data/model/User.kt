package com.racjonalnytraktor.findme3.data.model

import com.facebook.AccessToken

class User(
    var facebookId: String = "",
    var profileUri: String = "",
    var fullName: String = "",
    var token: String = "")