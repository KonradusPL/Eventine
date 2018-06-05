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
import com.racjonalnytraktor.findme3.ui.map.MapActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {

    val CHANNEL_DEFAULT_IMPORTANCE = "asdasdasd"
    val NOTIFICATION_ID_PING = 123123

    lateinit var notification: Notification
    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManagerCompat

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        if(message == null)
            return

        val action = message.data["action"].orEmpty()
        if (action.isEmpty())
            return

        if (action == "pingCreate"){
            val title = message.data["title"].orEmpty()
            val desc = message.data["desc"].orEmpty()
            createPingNotification(title,desc)
        }
        else{
            val groupName = message.data["groupName"].orEmpty()
            createGroupNotification(groupName)
        }

    }

    private fun createPingNotification(title: String, desc: String){
        val notificationIntent = Intent(this, MapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.logo_drawer)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID_PING, notificationBuilder.build())
    }
    private fun createGroupNotification(groupName: String){
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Zaproszenie do grupy $groupName")
                .setContentText("Kliknij aby dołączyć")
                .setSmallIcon(R.drawable.logo_drawer)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID_PING, notificationBuilder.build())


    }
}