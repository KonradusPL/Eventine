package com.racjonalnytraktor.findme3.data.messaging

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import android.media.AudioManager.RINGER_MODE_VIBRATE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log


class MyFirebaseMessagingService: FirebaseMessagingService() {

    val CHANNEL_DEFAULT_IMPORTANCE = "asdasdasd"
    val NOTIFICATION_ID_PING = 123123
    val NOTIFICATION_ID_HELP = 121212

    lateinit var notification: Notification
    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManagerCompat

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.d("onMessageReceived",message?.data.toString())

        if(message == null)
            return

        val action = message.data["action"].orEmpty()
        if (action.isEmpty())
            return

        when (action) {
            "pingCreate" -> {
                val title = message.data["title"].orEmpty()
                val desc = message.data["desc"].orEmpty()
                onCreatePing(title,desc)
            }
            "infoCreate" -> {
                val content = message.data["content"].orEmpty()
                onInfoCreate(content)
            }
            "findOrganizer" -> {
                val data = message.data
                onHelp(data["title"].orEmpty(),data["desc"].orEmpty(),data["callerId"].orEmpty())
            }
            "acceptRequest" -> {
                val data = message.data
                onAcceptHelp(data["desc"].orEmpty(),data["title"].orEmpty())
            }
            else -> {
                val groupName = message.data["groupName"].orEmpty()
                onCreateGroup(groupName)
            }
        }

        makeSound()

    }

    private fun onInfoCreate(content: String){
        val notificationIntent = Intent(this, MapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Informacja")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_info_outline_white_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID_PING, notificationBuilder.build())

        makeSound()
    }

    private fun onCreatePing(title: String, desc: String){
        val notificationIntent = Intent(this, MapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_pin_drop_black_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID_PING, notificationBuilder.build())

        makeSound()
    }
    private fun onCreateGroup(groupName: String){
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra("action","invite")
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Zaproszenie do grupy $groupName")
                .setContentText("Kliknij aby dołączyć")
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID_PING, notificationBuilder.build())

        makeSound()
    }

    private fun onAcceptHelp(desc: String, title: String){
        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(desc)
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID_PING, notificationBuilder.build())

        makeSound()
    }



    private fun onHelp(title: String, desc: String, id: String){
        val notificationIntent = Intent(this, MapActivity::class.java)
        notificationIntent.putExtra("action","help")
        val pendingIntent = PendingIntent.getActivity(this, 123, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val yesIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = "yes"
            putExtra("id",id)
        }
        val yesPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val noIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = "no"
            putExtra("id",id)
        }
        val noPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, noIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationManager = NotificationManagerCompat.from(this)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_add_black_24dp,"TAK",yesPendingIntent)
                .addAction(R.drawable.ic_not_interested_black_24dp,"NIE",noPendingIntent)
                .setAutoCancel(true)


        notificationManager.notify(NOTIFICATION_ID_HELP, notificationBuilder.build())
    }

    fun makeSound(){
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val mode = audio.ringerMode
        Log.d("RINGER_MODE",mode.toString())
        Log.d("RINGER_MODE_NORMAL",AudioManager.RINGER_MODE_NORMAL.toString())
        Log.d("RINGER_MODE_SILENT", RINGER_MODE_SILENT.toString())
        Log.d("RINGER_MODE_VIBRATE", RINGER_MODE_VIBRATE.toString())

        if (vibrator.hasVibrator() &&(mode == AudioManager.RINGER_MODE_NORMAL || mode == AudioManager.MODE_RINGTONE)) {
            val mVibratePattern = longArrayOf(0, 400, 200, 400)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createWaveform(mVibratePattern,-1)
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(mVibratePattern,-1)

            }
        }
    }

}