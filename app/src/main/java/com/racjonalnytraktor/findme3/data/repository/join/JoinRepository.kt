package com.racjonalnytraktor.findme3.data.repository.join

import com.racjonalnytraktor.findme3.data.model.Invitation
import io.reactivex.Observable


interface JoinRepository {
    fun getInvitations(): Observable<List<Invitation>>
}