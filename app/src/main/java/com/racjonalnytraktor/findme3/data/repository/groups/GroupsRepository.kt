package com.racjonalnytraktor.findme3.data.repository.groups

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable

object GroupsRepository: BaseRepository() {
    fun getTasks(): Observable<List<Task>>{
        return Observable.just(arrayListOf(Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204")))
    }
    fun getGroups(): Observable<List<Group>>{
        val token = prefs.getUserToken()
        return rest.networkService.getGroups(token)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .map { t -> t.groups }
    }
}