package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.model.UserFacebook
import com.racjonalnytraktor.findme3.data.network.model.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.LoginResponse
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import com.racjonalnytraktor.findme3.utils.WhereIsJson
import io.reactivex.Observable
import io.reactivex.Single

class LoginRepository: BaseRepository() {

    fun loginWithEmail(loginRequest: LoginRequest): Observable<LoginResponse>{
        return mRest.networkService.login(loginRequest)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }

    fun updateUserWhenSignIn(userId: String){

    }

    fun getUserBasicInfo(): Single<UserFacebook>{
        return mFacebookNetwork.getUserBasicInfo()
                .map { t -> WhereIsJson.getUserBasic(t.jsonObject) }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}