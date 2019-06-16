package com.racjonalnytraktor.findme3.ui

import android.app.*
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import android.util.Log
import android.view.Gravity
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.local.migration.MyMigration
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.utils.ZoneUtils
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import es.dmoral.toasty.Toasty
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.longToast

class AppClass: Application(),ZoneUtils.BeaconListener {

    val beaconUtils = ZoneUtils

    private lateinit var mRepo: MapRepository
    private val cloudCredentials = EstimoteCloudCredentials("indoorlocation-m4a","846401acdfecd6753a2d69750172aa67")
    private var mObservationHandler: ProximityObserver.Handler? = null

    fun initBeaconsScanning(activity: Activity){

        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel("my_service", "My Background Service")
                } else {
                    // If earlier version channel ID is not used
                    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                    ""
                }

        val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Skanowanie beaconów")
                .setContentText("Skanowanie trwa...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        val proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                .withBalancedPowerMode()
                .withEstimoteSecureMonitoringDisabled()
                .withTelemetryReportingDisabled()
                .withScannerInForegroundService(notification)
                .onError { throwable: Throwable ->  Log.d("Beacons",throwable.toString()) }
                .build()

        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                activity,
                onRequirementsFulfilled = {
                    Log.d("Beacons","onRequirementsFulfilled")
                    Log.d("Beacons",beaconUtils.beaconZones.size.toString())
                    if (mObservationHandler == null){
                        mObservationHandler = proximityObserver.startObserving(beaconUtils.beaconZones)
                    }
                },
                onRequirementsMissing = {},
                onError = {}
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onEnterZone(tag: String) {
        val toast = Toasty.info(this,"Jesteś w $tag")
        toast.setGravity(Gravity.TOP.xor(Gravity.CENTER_HORIZONTAL),0,64)
        toast.show()

        val token = mRepo.prefs.getUserToken()
        val map = HashMap<String,Any>()
        map["groupId"] = mRepo.prefs.getCurrentGroupId()
        map["locationTag"] = tag
        mRepo.rest.networkService.updateLocation(token,map)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: String? ->
                    Log.d("updateLocation",t)
                },{t: Throwable? ->
                    Log.d("updateLocation",t.toString())
                })
        Log.d("Beacons","Enter")
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .schemaVersion(4)
                .migration(MyMigration())
                .build()
        Realm.setDefaultConfiguration(config)
        mRepo = MapRepository
        beaconUtils.listener = this
    }

    fun changeBeaconsStatus(enable: Boolean, activity: Activity){
        if (enable)
            initBeaconsScanning(activity)
        else{
            mObservationHandler?.stop()
            mObservationHandler = null
        }
    }
}