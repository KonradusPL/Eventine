package com.racjonalnytraktor.findme3.data.network.model.createping

import com.google.gson.annotations.SerializedName
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed

data class Ping(
        var groupId: String = "",
        var creator: String = "",
        var title: String = "",
        var targetGroups: ArrayList<String> = ArrayList(),
        var desc: String = "",
        var geo: ArrayList<Double> = ArrayList(),
        var inProgress: String = "",
        var done: String = "",
        @SerializedName("_id")var pingId: String = "",
        var creatorName: String = ""): Typed()