package com.racjonalnytraktor.findme3.data.repository.create

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.CreateGroupRequest
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single

object CreateRepository: BaseRepository(){

    fun getFriends(): Observable<User>{

        return Observable.just(User(fullName = "Adam Nowak"),
                User(fullName = "Jakub Kowalski"),
                User(fullName = "Patryk Godek"))

        /*val facebookObservable =
                if(AccessToken.getCurrentAccessToken() != null)
                    facebook.getFriends()
                     .map { t -> WhereIsJson.getFriendsArray(t.jsonObject) }
                else
                    Observable.empty()

        return rest.networkService.getFriends(prefs.getUserToken())
                .map { t -> t.users }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())*/
    }

    fun getFriendWithPicture(user: User): Single<User>{
        return facebook.getFriendWithPicture(user.facebookId,user)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun createGroup(createGroupRequest: CreateGroupRequest): Single<String>{
        val token = prefs.getUserToken()
        Log.d("token",token)
        return rest.networkService.createGroup(token,createGroupRequest)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

}