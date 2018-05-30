package com.racjonalnytraktor.findme3.data.repository.create

import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import com.racjonalnytraktor.findme3.utils.WhereIsJson
import io.reactivex.Observable
import io.reactivex.Single

object CreateRepository: BaseRepository(){

    fun getFriends(): Observable<User>{
        return mFacebook.getFriends()
                .map { t -> WhereIsJson.getFriendsArray(t.jsonObject) }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun getFriendWithPicture(user: User): Single<User>{
        return mFacebook.getFriendWithPicture(user.facebookId,user)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

}