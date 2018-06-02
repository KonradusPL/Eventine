package com.racjonalnytraktor.findme3.data.network.model.info

/**
 * Created by Admin on 2018-06-02.
 */
data class Info(
        var groupId: String = "",
        var content: String = "",
        var targetGroups: ArrayList<String> = ArrayList()
)