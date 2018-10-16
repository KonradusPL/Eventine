package com.racjonalnytraktor.findme3.data.model.new

import kotlin.collections.ArrayList

data class CreateActionRequest(
        var groupId: String = "",
        var type: String = "",
        var title: String = "",
        var desc: String = "",
        var targetGroups: ArrayList<String> = ArrayList(),
        var people: ArrayList<String> = ArrayList(),
        var plannedTime: String = "",
        var geo: ArrayList<Double> = arrayListOf(0.0,0.0),
        var floor: Int = 0
){
    override fun toString(): String{
        return "groupId: $groupId, type: $type, title: $title, desc: $desc, plannedTime: $plannedTime, " +
                "geo: $geo, people: $people, floor: $floor"
    }
}