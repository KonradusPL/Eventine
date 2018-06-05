package com.racjonalnytraktor.findme3.data.repository.map

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single


object MapRepository: BaseRepository() {

    val newPing = Ping()
    val newInfo = Info()
    var type: String = "ping"
    var state: String = "basic"
    //val locationProvider = LocationProvider(1000,context)

    val pings = ArrayList<Ping>()

    fun clearData(){
        newPing.targetGroups.clear()
        newPing.desc = ""
        newPing.title = "asd"
        newInfo.targetGroups.clear()
        newInfo.content = ""
    }

    fun getAllSubGroups(): Observable<List<String>>{
        Log.d("zzzzzz",prefs.getUserToken())
        Log.d("zzzzzz",prefs.getCurrentGroupId())
        return rest.networkService.getAllSubGroups(prefs.getUserToken(),prefs.getCurrentGroupId())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun createPing(): Single<String>{
        Log.d("opopop",newPing.title)
        Log.d("request","${newPing.desc} ${newPing.title} ${newPing.geo} " +
                "${newPing.groupId} ${newPing.targetGroups} ")
        newPing.groupId = prefs.getCurrentGroupId()
        Log.d("title",newPing.title)
        Log.d("desc",newPing.desc)
        Log.d("targetGroups",newPing.targetGroups.toString())
        Log.d("groupId",newPing.groupId)
        Log.d("geo",newPing.geo.toString())

        return rest.networkService.createPing(prefs.getUserToken(),newPing)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())

    }

    fun getPings(): Observable<Ping>{
        return rest.networkService.getPings(prefs.getUserToken(),prefs.getCurrentGroupId())
                .map { t -> t.pings }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun createInfo(): Single<String>{
        newInfo.groupId = prefs.getCurrentGroupId()

        Log.d("content",newInfo.content)
        Log.d("groupId",newInfo.groupId)
        Log.d("targetGroups",newInfo.targetGroups.toString())

        return rest.networkService.createInfo(prefs.getUserToken(),newInfo)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun saveState(checked: List<String>,task: String, descr: String, type: String, state: String){
        Log.d("savestate",task)
        Log.d("savestate",descr)
        Log.d("savestate",type)
        this.state = state
        if(type == "ping"){
            newPing.title = task
            newPing.desc = descr
            newPing.targetGroups.clear()
            newPing.targetGroups.addAll(checked)
        }else{
            newInfo.content = descr
            newInfo.targetGroups.clear()
            newInfo.targetGroups.addAll(checked)
        }
    }

    fun endPing(groupId: String): Single<String>{
        return rest.networkService.endPing(prefs.getUserToken(),groupId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

}