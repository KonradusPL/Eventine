package com.racjonalnytraktor.findme3.ui

import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.local.migration.MyMigration
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.utils.BeaconUtils
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import es.dmoral.toasty.Toasty
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread

class AppClass: Application(),BeaconUtils.BeaconListener {

    val beaconUtils = BeaconUtils

    private lateinit var mRepo: MapRepository
    private val cloudCredentials = EstimoteCloudCredentials("indoorlocation-m4a","846401acdfecd6753a2d69750172aa67")
    private var mObservationHandler: ProximityObserver.Handler? = null

    fun initBeaconsScanning(activity: Activity){

        val notification = NotificationCompat.Builder(this, "beacons scanning")
                .setSmallIcon(R.drawable.beacon_beetroot_small)
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

    override fun onEnterZone(tag: String) {
        longToast("Tag: $tag")
        val token = mRepo.prefs.getUserToken()
        val map = HashMap<String,Any>()
        map["groupId"] = mRepo.prefs.getCurrentGroupId()
        map["locationTag"] = "tag"
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
                .schemaVersion(1)
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