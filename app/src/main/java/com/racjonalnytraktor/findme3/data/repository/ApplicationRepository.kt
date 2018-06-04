package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.model.Group

object ApplicationRepository {

    val groups = ArrayList<Group>()

    fun addGroups(groups: ArrayList<Group>){
        groups.clear()
        groups.addAll(groups)
    }
    fun addGroup(group: Group) = groups.add(group)

    fun clearGroups() = groups.clear()

}