package com.racjonalnytraktor.findme3.data.model

import com.google.gson.annotations.SerializedName
import com.racjonalnytraktor.findme3.data.model.new.Relative
import java.util.*
import kotlin.collections.ArrayList

data class Action(
        @SerializedName("_id") var id: String = "",
        var groupId: String = "",
        var type: String = "",
        var createdAt: Date = Date(),
        var creator: Relative = Relative(),
        var title: String = "",
        var desc: String = "",
        var targetGroups: ArrayList<String> = ArrayList(),
        var plannedTime: Date = Date(),
        var geo: ArrayList<Double> = ArrayList(),
        var status: String = "",
        var progressor: Relative = Relative(),
        var executor: Relative = Relative(),
        var floor: Int = 0)