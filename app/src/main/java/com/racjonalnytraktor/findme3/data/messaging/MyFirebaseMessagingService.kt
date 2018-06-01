package com.racjonalnytraktor.findme3.data.messaging

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.main.MainActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {

    val CHANNEL_DEFAULT_IMPORTANCE = "asdasdasd"
    val ONGOING_NOTIFICATION_ID = 123123

    lateinit var notification: Notification
    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManagerCompat

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        val groupName = message?.data?.get("groupName")
        val action = message?.data?.get("action")

       // Log.d("message",groupName)
        //Log.d("action",action)

        createNotification(groupName.orEmpty(), action.orEmpty())
    }
    private fun createNotification(groupName: String, action: String){
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Zaproszenie do grupy ${groupName}")
                .setContentText("Dawid pisz htmla")
                .setSmallIcon(R.drawable.logo_tinder_splash)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build())


    }
}