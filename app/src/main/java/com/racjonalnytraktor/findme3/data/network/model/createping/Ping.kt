package com.racjonalnytraktor.findme3.data.network.model.createping

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.racjonalnytraktor.findme3.data.model.Action
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
        var progressorName: String = "",
        @SerializedName("_id")var pingId: String = "",
        var creatorName: String = "",
        var createdAt: String = "",
        var floor: Int = -1)

        : Typed(){

    constructor(action: Action) : this() {
        fromAction(action)
    }

    fun clone(ping: Ping){
        groupId = groupId.plus(ping.groupId)
        creator = creator.plus(ping.creator)
        title = title.plus(ping.title)
        targetGroups.addAll(ping.targetGroups)
        desc = desc.plus(ping.desc)
        geo = ArrayList()
        inProgress = ping.inProgress
        createdAt = "".plus(ping.createdAt)
        done = done.plus(ping.done)
        pingId = pingId.plus(ping.pingId)
        creatorName = creatorName.plus(ping.creatorName)
        progressorName = "".plus(ping.progressorName)
        floor = ping.floor
    }

    fun fromAction(action: Action){
        if (action.status == "done")
            Log.d("fromAction",action.status)
        groupId = action.groupId
        creator = action.creator.id
        title = action.title
        targetGroups.addAll(action.targetGroups)
        desc = action.desc
        geo = action.geo
        createdAt = action.createdAt.toString()
        pingId = pingId.plus(action.id)
        creatorName = action.creator.name
        progressorName = action.progressor.name
        floor = action.floor
        when(action.status){
            "inProgress"-> inProgress = true
            "done" -> ended = true
        }
        if(ended)
            Log.d("fromAction","works ;)")

    }
}