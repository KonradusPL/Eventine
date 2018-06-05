package com.racjonalnytraktor.findme3.data.repository.groups

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.repository.ApplicationRepository.groups
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable

object GroupsRepository: BaseRepository() {

    fun getTasks(): Observable<List<Task>> {
     TODO()
    }

    fun getGroups(): Observable<ArrayList<Group>>{
        val token = prefs.getUserToken()
        val observable =  rest.networkService.getGroups(token)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .map { t -> t.groups }

        observable.subscribe({t: ArrayList<Group>? ->
            Log.d("xxxxxxx","xxxxxxx")
            appRepo.addGroups(t!!)
        },{t: Throwable? ->

        })
        return observable
    }
}