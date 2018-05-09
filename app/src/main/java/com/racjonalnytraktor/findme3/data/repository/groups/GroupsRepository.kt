package com.racjonalnytraktor.findme3.data.repository.groups

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import io.reactivex.Observable

interface GroupsRepository {
    fun getTasks(): Observable<List<Task>>
    fun getGroups(): Observable<List<Group>>
}