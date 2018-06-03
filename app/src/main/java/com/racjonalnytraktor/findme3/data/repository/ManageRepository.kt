package com.racjonalnytraktor.findme3.data.repository

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.ChangeSubGroupRequest
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Header
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.changegroups.UserInSubGroup
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Admin on 2018-06-03.
 */
object ManageRepository: BaseRepository() {

    fun getPeopleInGroups(): Observable<Typed>{
        return rest.networkService.getPeopleInSubGroups(prefs.getUserToken(),prefs.getCurrentGroup())
                .map { t -> getTypedArray(t.people) }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getTypedArray(oldArray: ArrayList<UserInSubGroup>): ArrayList<Typed>{
        val subGroups = ArrayList<String>()
        for(item in oldArray){
            if(!subGroups.contains(item.subgroup))
                subGroups.add(item.subgroup)
        }
        val newArray = ArrayList<Typed>()
        for(item in subGroups){
            val header = Header(item)
            header.type = "header"
            newArray.add(header)
            var i = 0
            while (true){
                if(i == oldArray.size)
                    break

                if(oldArray[i].subgroup == item){
                    Log.d("cccc",oldArray[i].name + "asd")
                    Log.d("cccc",oldArray[i].id + "asd")
                    Log.d("cccc",oldArray[i].subgroup + "asd")
                    oldArray[i].type = "person"
                    newArray.add(oldArray[i])
                }
                i++

            }
        }
        return newArray
    }

    fun changeSubGroups(request: ChangeSubGroupRequest): Single<String> {
        return rest.networkService.changeSubGroups(prefs.getUserToken(),request)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}