package com.racjonalnytraktor.findme3.data.model.new

import java.util.*
import kotlin.collections.ArrayList

data class CreateActionRequest(
        var groupId: String = "",
        var type: String = "",
        var title: String = "",
        var descr: String = "",
        var targetGroups: ArrayList<String> = ArrayList(),
        var people: ArrayList<String> = ArrayList(),
        var plannedTime: String = "",
        var geo: ArrayList<Double> = arrayListOf(0.0,0.0)
){
    override fun toString(): String{
        return "groupId: $groupId, type: $type, title: $title, descr: $descr, plannedTime: $plannedTime, " +
                "geo: $geo, people: $people"
    }
}