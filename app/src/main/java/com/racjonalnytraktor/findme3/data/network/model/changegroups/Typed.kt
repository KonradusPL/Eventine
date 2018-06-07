package com.racjonalnytraktor.findme3.data.network.model.changegroups

import com.google.gson.annotations.SerializedName

open class Typed {
    var type = ""
    @SerializedName("plannedTime") var date = ""
}