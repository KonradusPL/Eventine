package com.racjonalnytraktor.findme3.data.network.model

import com.racjonalnytraktor.findme3.data.model.Group

/**
 * Created by Admin on 2018-05-31.
 */
data class GroupsResponse (
    val groups: ArrayList<Group>
){
    override fun toString(): String {
        return groups.toString()
    }
}