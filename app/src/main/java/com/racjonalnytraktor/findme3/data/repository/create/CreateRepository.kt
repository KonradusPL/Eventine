package com.racjonalnytraktor.findme3.data.repository.create

import com.racjonalnytraktor.findme3.data.model.Person
import io.reactivex.Observable

interface CreateRepository {
    fun getFriends(): Observable<List<Person>>
}