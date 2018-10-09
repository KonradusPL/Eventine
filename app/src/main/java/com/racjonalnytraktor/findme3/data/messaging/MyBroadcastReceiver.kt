package com.racjonalnytraktor.findme3.data.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.SchedulerProvider

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MyBroadcastReceiver","onReceive")
        val repo = MapRepository
        if(intent != null){
            val token = repo.prefs.getUserToken()
            val data = HashMap<String, Any>()
            data["callerId"] = intent.extras["id"]
            data["response"] = intent.action == "yes"
            Log.d("sendNotifResponse",data.toString())
            repo.rest.networkService.sendNotifResponse(token,data)
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe({ t: String? ->
                        Log.d("sendNotifResponse",t.orEmpty())
                    },{t: Throwable? ->
                        Log.d("sendNotifResponse",t.toString())
                    })
        }
    }
}