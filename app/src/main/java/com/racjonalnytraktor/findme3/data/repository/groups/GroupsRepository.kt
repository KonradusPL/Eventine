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
import java.util.concurrent.TimeUnit

object GroupsRepository: BaseRepository() {

    fun getTasks(): Observable<Ping> {
     /*return rest.networkService.getTasks(preferences.getUserToken())
             .map { t -> t.pings }
             .flatMapIterable { t -> t }
             .subscribeOn(SchedulerProvider.io())
             .observeOn(SchedulerProvider.ui())*/

        val arrayList = ArrayList<Ping>()
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        arrayList.add(Ping(title = "Lorem ipsum dolor", desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", createdAt = "10:13 AM"))
        return Observable.just(arrayList)
                .flatMapIterable { t -> t }
                .delay(4L, TimeUnit.SECONDS)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())


    }

    fun getGroups(): Observable<GroupWithUsers>{
        val groups = ArrayList<GroupWithUsers>()
        //val group = Groc
        val arrayList = ArrayList<Person>()
        arrayList.add(Person("Anna Kowalska","",""))
        arrayList.add(Person("Adam Nowak","",""))
        arrayList.add(Person("Jan Kowalski","",""))
        groups.add(GroupWithUsers(Group("Football Cup 2018","asd",""),arrayList))
        groups.add(GroupWithUsers(Group("Kalejdoskop","asd",""),arrayList))
        groups.add(GroupWithUsers(Group("Metallica Cracow","asd",""),arrayList))
        return Observable.just(groups)
                .delay(4L,TimeUnit.SECONDS)
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())

        /*val token = preferences.getUserToken()
        val observable =  rest.networkService.getGroups(token)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .map { t -> t.groups }
                .flatMapIterable { t -> t }
                .map { t ->  GroupWithUsers(t,arrayList)}

        appRepo.clearGroups()

        observable.subscribe({t: GroupWithUsers? ->
            appRepo.addGroup(t!!.group)
        },{t: Throwable? ->

        })
        return observable*/
    }
}