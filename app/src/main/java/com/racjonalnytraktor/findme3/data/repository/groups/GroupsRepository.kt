package com.racjonalnytraktor.findme3.data.repository.groups

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.GroupWithUsers
import com.racjonalnytraktor.findme3.data.model.Person
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.repository.ApplicationRepository.groups
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable

object GroupsRepository: BaseRepository() {

    fun getTasks(): Observable<Ping> {
     return rest.networkService.getTasks(prefs.getUserToken())
             .map { t -> t.pings }
             .flatMapIterable { t -> t }
             .subscribeOn(SchedulerProvider.io())
             .observeOn(SchedulerProvider.ui())
    }

    fun getGroups(): Observable<ArrayList<GroupWithUsers>>{
        val groups = ArrayList<GroupWithUsers>()
        //val group = Groc
        val arrayList = ArrayList<Person>()
        arrayList.add(Person("Konrad Pękala","",""))
        arrayList.add(Person("Marcin Michno","",""))
        arrayList.add(Person("Andrzej Duda","",""))
        groups.add(GroupWithUsers(Group("Szachiści","asd",""),arrayList))
        groups.add(GroupWithUsers(Group("Kalejdoskop","asd",""),arrayList))
        groups.add(GroupWithUsers(Group("Karnawał Sztukmistrzów","asd",""),arrayList))
        return Observable.just(groups)


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
        //return observable
    }
}