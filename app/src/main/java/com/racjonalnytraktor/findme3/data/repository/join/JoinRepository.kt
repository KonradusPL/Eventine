package com.racjonalnytraktor.findme3.data.repository.join

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.AcceptInvitationRequest
import com.racjonalnytraktor.findme3.data.network.model.InvitationResponse
import com.racjonalnytraktor.findme3.data.network.model.JoinRequest
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

object JoinRepository: BaseRepository(){
     fun getInvitations(): Observable<InvitationResponse> {
         val token = preferences.getUserToken()

         return rest.networkService.getInvitations(token)
                 .subscribeOn(SchedulerProvider.io())
                 .observeOn(SchedulerProvider.ui())
     }

    fun joinGroup(groupName: String): Single<String>{
        val token = preferences.getUserToken()

        Log.d("tokenik",token)
        Log.d("tokenik",groupName)

        return rest.networkService.joinGroup(token, JoinRequest(groupName))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun acceptInvitation(groupId: String): Single<String>{
        val request = AcceptInvitationRequest(groupId)
        val token = preferences.getUserToken()
        return rest.networkService.acceptInvitation(token,request)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

}