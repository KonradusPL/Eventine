package com.racjonalnytraktor.findme3.data.messaging

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider

class MyFirebaseinstanceId: FirebaseInstanceIdService() {

    private val TAG = "MyFirebaseIDService"

    val repo = BaseRepository()

    override fun onCreate() {
        super.onCreate()
        repo.onAttatch(this)
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)
        sendTokenToServer(refreshedToken)
    }

    private fun sendTokenToServer(token: String){
        /*Log.d("tokenik",token)
        Log.d("header",repo.prefs.getUserToken())
        repo.rest.networkService.updateNotifToken(repo.prefs.getUserToken(),token)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("tokenik",t)
                },{t: Throwable? ->
                    Log.d("tokenik",t!!.message)
                })*/
    }
}