package com.racjonalnytraktor.findme3.data.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import org.jetbrains.anko.toast

class MyBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MyBroadcastReceiver","onReceive")
        if(context == null)
            return
        val repo = MapRepository
        val action = intent?.action ?: ""
        if(intent != null && ( action == "yes" || action == "no")){
            val notifId = intent.extras["notifId"]

            if(notifId != null)
                NotificationManagerCompat.from(context).cancel(notifId as Int)

            val token = repo.preferences.getUserToken()
            val data = HashMap<String, Any>()
            data["callerId"] = intent.extras["id"]
            data["response"] = intent.action == "yes"
            Log.d("sendNotifResponse",data.toString())
            repo.rest.networkService.sendNotifResponse(token,data)
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe({ t: String? ->
                        context?.toast("Zaakceptowano helpa")
                        Log.d("sendNotifResponse",t.orEmpty())
                    },{t: Throwable? ->
                        context?.toast("timeout")
                        Log.d("sendNotifResponse",t.toString())
                    })
        }
    }
}