package com.racjonalnytraktor.findme3.data.repository.create

import com.racjonalnytraktor.findme3.data.model.Friend
import io.reactivex.Flowable
import io.reactivex.Observable

interface CreateRepository {
    fun getFriends(): Observable<List<Friend>>
}