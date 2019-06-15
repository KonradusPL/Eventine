package com.racjonalnytraktor.findme3.data.repository

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.ChangeSubGroupRequest
import com.racjonalnytraktor.findme3.data.network.model.UserSimple
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Header
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.changegroups.UserInSubGroup
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

object ManageRepository: BaseRepository() {

    fun getPeopleInGroups(): Observable<Typed>{
        return rest.networkService.getGroupMembers(preferences.getUserToken(),preferences.getCurrentGroupId())
                .map { t -> getTypedArray(t.people) }
                .toObservable()
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getTypedArray(oldArray: ArrayList<UserSimple>): ArrayList<Typed> {
        Log.d("qqqq", oldArray.toString())
        val subGroups = ArrayList<String>()
        for (item in oldArray) {
            if (!subGroups.contains(item.subgroup))
                subGroups.add(item.subgroup)
        }
        val newArray = ArrayList<Typed>()
        for (item in subGroups) {
            val header = Header(item)
            header.type = "header"
            newArray.add(header)
            for (oldItem in oldArray) {
                if (oldItem.subgroup == item) {
                    val userInSubGroup = UserInSubGroup(oldItem.id, oldItem.name, oldItem.subgroup)
                    userInSubGroup.type = "person"
                    newArray.add(userInSubGroup)
                }

            }
        }
        return newArray
    }


    fun changeSubGroups(request: ChangeSubGroupRequest): Single<String> {
        return rest.networkService.changeSubGroups(preferences.getUserToken(),request)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}