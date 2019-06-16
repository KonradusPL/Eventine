package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.GroupsEntity
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Single

class UserRepository(): BaseRepository() {
    fun getGroupList(): Single<List<Group>>
            = rest.networkService.getGroupList(prefs.getUserToken())
            .map { entity: GroupsEntity -> entity.groups }
            .subscribeOn(SchedulerProvider.io())
            .observeOn(SchedulerProvider.ui())
}