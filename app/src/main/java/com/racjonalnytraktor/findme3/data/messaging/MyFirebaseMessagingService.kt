package com.racjonalnytraktor.findme3.data.messaging

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import android.media.AudioManager.RINGER_MODE_VIBRATE
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import java.util.*
import android.media.RingtoneManager
import android.media.Ringtone
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import org.jetbrains.anko.runOnUiThread


class MyFirebaseMessagingService: FirebaseMessagingService() {

    val CHANNEL_DEFAULT_IMPORTANCE = "asdasdasd"
    val NOTIFICATION_ID_PING = 123123
    val NOTIFICATION_ID_HELP = 121212

    lateinit var notification: Notification
    lateinit var notificationBuilder: NotificationCompat.Builder
    var notificationManager: NotificationManagerCompat? = null
    lateinit var repo: BaseRepository

    override fun onCreate() {
        super.onCreate()
        repo = BaseRepository()
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.d("onMessageReceived",message?.data.toString())

        if(message == null)
            return

        val action = message.data["action"].orEmpty()
        if (action.isEmpty())
            return

        when (action) {
            "create" -> {
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
            "callCaretaker" -> {
                val data = message.data
                onCallCareTaker(data["desc"].orEmpty(),data["title"].orEmpty())
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

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Informacja")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_info_outline_white_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager?.notify(NOTIFICATION_ID_PING, notificationBuilder.build())

        makeSound()
    }

    private fun onCreatePing(title: String, desc: String){
        val notificationIntent = Intent(this, MapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val id = Random().nextInt(100000)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_pin_drop_black_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager?.notify(id, notificationBuilder.build())

        makeSound()
    }
    private fun onCreateGroup(groupName: String){
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra("action","invite")
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val id = Random().nextInt(100000)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle("Zaproszenie do grupy $groupName")
                .setContentText("Kliknij aby dołączyć")
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager?.notify(id, notificationBuilder.build())

        makeSound()
    }

    private fun onAcceptHelp(desc: String, title: String){
        val id = Random().nextInt(100000)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(desc)
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager?.notify(id, notificationBuilder.build())

        makeSound()
    }


    private fun onCallCareTaker(desc: String, title: String){
        val id = Random().nextInt(100000)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(desc)
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        notificationManager?.notify(id, notificationBuilder.build())

        makeSound()
    }



    private fun onHelp(title: String, desc: String, id: String){
        val notificationIntent = Intent(this, MapActivity::class.java)
        notificationIntent.putExtra("action","help")
        val pendingIntent = PendingIntent.getActivity(this, 123, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val notifId = Random().nextInt(100000)

        val yesIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = "yes"
            putExtra("id",id)
            putExtra("notifId",notifId)
        }
        val yesPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val noIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = "no"
            putExtra("id",id)
            putExtra("notifId",notifId)
        }
        val noPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 123, noIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
                .setContentTitle(title)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_event_white_24dp)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_add_black_24dp,"TAK",yesPendingIntent)
                .addAction(R.drawable.ic_not_interested_black_24dp,"NIE",noPendingIntent)
                .setAutoCancel(true)


        notificationManager?.notify(notifId, notificationBuilder.build())

        makeSound()
    }

    private fun makeSound(){
        runOnUiThread {
            val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            val mode = audio.ringerMode
            Log.d("RINGER_MODE",mode.toString())
            Log.d("RINGER_MODE_NORMAL",AudioManager.RINGER_MODE_NORMAL.toString())
            Log.d("RINGER_MODE_SILENT", RINGER_MODE_SILENT.toString())
            Log.d("RINGER_MODE_VIBRATE", RINGER_MODE_VIBRATE.toString())

            if (repo.prefs.isSilentNotification())
                return@runOnUiThread

            if(mode == AudioManager.RINGER_MODE_NORMAL){
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(this, uri)
                ringtone.play()
            }

            else if (vibrator.hasVibrator() &&(mode == AudioManager.RINGER_MODE_VIBRATE)) {
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

}