package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import android.util.Log
import com.facebook.share.Share
import com.racjonalnytraktor.findme3.data.local.Prefs
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.data.local.db.RealmLocalDb
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.facebook.FacebookNetwork
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbResponse
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Single

open class BaseRepository {
    val rest = RetrofitRest()
    val facebook = FacebookNetwork()
    val appRepo = ApplicationRepository
    val prefs: Prefs = RealmLocalDb()

    fun saveUser(token: String,facebookId: String,email: String, isPartner: Boolean, groupId: String  = ""){
        val user = User(facebookId,"","",token,"",email)
        prefs.setFacebookId(facebookId)
        prefs.setUserEmail(email)
        prefs.setUserToken(token)
        prefs.setIsPartner(isPartner)
        prefs.setCurrentGroupId(groupId)
        prefs.setCurrentGroupId("5bcf0097f312e400106a1943")
        prefs.setIsUserLoggedIn(true)
    }

    fun registerByFacebook(user: User): Single<RegisterFbResponse> {
        prefs.createUser(user)
        val request = RegisterFbRequest(user.facebookId, user.fullName)
        Log.d("requestid",request.facebookId)
        Log.d("requestname",request.fullName)

        return rest.networkService.registerByFacebook(request)
                .map { t -> RegisterFbResponse(t.success,t.token,user.facebookId) }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}