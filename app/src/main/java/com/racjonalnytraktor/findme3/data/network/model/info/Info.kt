package com.racjonalnytraktor.findme3.data.network.model.info

import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed

data class Info(
        var groupId: String = "",
        var content: String = "",
        var targetGroups: ArrayList<String> = ArrayList(),
        var creatorName: String = "",
        var createdAt: String = ""): Typed()