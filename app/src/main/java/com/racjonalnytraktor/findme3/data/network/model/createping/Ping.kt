package com.racjonalnytraktor.findme3.data.network.model.createping

data class Ping(
        var groupId: String = "",
        var creator: String = "",
        var title: String = "",
        var targetGroups: ArrayList<String> = ArrayList(),
        var desc: String = "",
        var geo: ArrayList<Double> = ArrayList()
)