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
        var inProgress: Boolean = false,
        var ended: Boolean = false,
        var done: String = "",
        @SerializedName("_id")var pingId: String = "",
        var creatorName: String = ""): Typed(){

    fun clone(ping: Ping){
        groupId = groupId.plus(ping.groupId)
        creator = creator.plus(ping.creator)
        title = title.plus(ping.title)
        targetGroups.addAll(ping.targetGroups)
        desc = desc.plus(ping.desc)
        geo = ArrayList()
        inProgress = ping.inProgress
        done = done.plus(ping.done)
        pingId = pingId.plus(ping.pingId)
        creatorName = creatorName.plus(ping.creatorName)
    }
}