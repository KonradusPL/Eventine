package com.racjonalnytraktor.findme3.data.repository.map

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.EndPing
import com.racjonalnytraktor.findme3.data.network.model.UserSimple
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.utils.ClassTransform
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
    val actions = ArrayList<Action>()

    val members = ArrayList<UserSimple>()

    fun clearData(){
        newPing.targetGroups.clear()
        newPing.desc = ""
        newPing.title = ""
        newInfo.targetGroups.clear()
        newInfo.content = ""
    }

    fun getAllSubGroups(token: String, groupId: String): Observable<List<String>>{

        return rest.networkService.getAllSubGroups(token,groupId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun createPing(): Single<String>{
        Log.d("opopop",newPing.title)
        Log.d("request","${newPing.desc} ${newPing.title} ${newPing.geo} " +
                "${newPing.groupId} ${newPing.targetGroups} ${newPing.date} ")
        //newPing.groupId = prefs.getCurrentGroupId()
        Log.d("title",newPing.title)
        Log.d("desc",newPing.desc)
        Log.d("targetGroups",newPing.targetGroups.toString())
        Log.d("groupId",newPing.groupId)
        Log.d("geo",newPing.geo.toString())
        //newPing.creatorName = prefs.getUserFullName()

        return rest.networkService.createPing("",newPing)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())

    }

    fun getPings(): Observable<List<Ping>>{
        val array = ArrayList<Ping>()
        array.add(Ping(pingId = "1",title = "Go to kitchen",geo = arrayListOf(51.101850,22.853889),desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."))
        array.add(Ping(pingId = "2",title = "Please, remove rubbish",geo = arrayListOf(51.101628,22.853626),desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."))
        array.add(Ping(pingId = "3",title = "meeting in the dining room",geo = arrayListOf(51.101599,22.854527),desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."))
        return Observable.just(array.toList())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())

        /*return rest.networkService.getPings(prefs.getUserToken(),prefs.getCurrentGroupId())
                .map { t -> t.pings.toList() }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())*/
    }

    fun createInfo(): Single<String>{
       // newInfo.groupId = prefs.getCurrentGroupId()

        Log.d("content",newInfo.content)
        Log.d("groupId",newInfo.groupId)
        Log.d("targetGroups",newInfo.targetGroups.toString())

        return rest.networkService.createInfo("",newInfo)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun createAction(action: CreateActionRequest): Single<String>{
        val token = prefs.getUserToken()
        Log.d("tokenik",token)

        return rest.networkService.createAction(token,action)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getMapPings(): Single<ArrayList<Action>>{
        val token = prefs.getUserToken()
        val groupId = prefs.getCurrentGroupId()
        return rest.networkService.getActions(token,groupId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getMembers(): Single<ArrayList<Job>>{
        val token = prefs.getUserToken()
        val groupId = prefs.getCurrentGroupId()
        return rest.networkService.getGroupMembers(token, groupId)
                .map { t -> t.people }
                .map { t -> ClassTransform.fromPeopleArrayToJobs(t) }
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

    fun endPing(pingId: String): Single<String>{
        val request = EndPing(pingId)
        return rest.networkService.endPing("",request)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun inProgressPing(pingId: String): Single<String>{
        val request = EndPing(pingId)
        return rest.networkService.setPingToInProgress("",request)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getJobs(members: ArrayList<UserSimple>){

    }

}