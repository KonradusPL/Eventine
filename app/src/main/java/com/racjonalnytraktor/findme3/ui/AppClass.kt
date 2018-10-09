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
import com.racjonalnytraktor.findme3.data.repository.map.MapRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import es.dmoral.toasty.Toasty
import io.realm.Realm
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AppClass: Application() {

    private val cloudCredentials = EstimoteCloudCredentials("indoorlocation-m4a","846401acdfecd6753a2d69750172aa67")
    private var mObservationHandler: ProximityObserver.Handler? = null

    fun initBeaconsScanning(activity: Activity){
        val mRepo = MapRepository

        val notification = NotificationCompat.Builder(this, "beacons scanning")
                .setSmallIcon(R.drawable.beacon_beetroot_small)
                .setContentTitle("Beacon scan")
                .setContentText("Scan is running...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        val proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                .withBalancedPowerMode()
                .withScannerInForegroundService(notification)
                .onError { Log.d("Beacons","proximityObserver error") }
                .build()
        val pokoikZone = ProximityZoneBuilder()
                .forTag("Pokoik")
                .inCustomRange(1.0)
                .onEnter {
                    val token = mRepo.prefs.getUserToken()
                    val map = HashMap<String,Any>()
                    map["groupId"] = mRepo.prefs.getCurrentGroupId()
                    map["locationTag"] = "Pokoik"
                    mRepo.rest.networkService.updateLocation(token,map)
                            .subscribeOn(SchedulerProvider.io())
                            .observeOn(SchedulerProvider.ui())
                            .subscribe({t: String? ->
                                Log.d("updateLocation",t)
                            },{t: Throwable? ->
                                Log.d("updateLocation",t.toString())
                            })
                    Toasty.info(this@AppClass,"Enter!").show()
                    Log.d("Beacons","Enter")
                }
                .onExit {
                    Toasty.info(this@AppClass,"Exit!").show()
                    Log.d("Beacons","Exit")
                }
                .onContextChange {/* do something here */}
                .build()
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                activity,
                onRequirementsFulfilled = {
                    Log.d("Beacons","onRequirementsFulfilled")
                    mObservationHandler = proximityObserver.startObserving(pokoikZone)
                },
                onRequirementsMissing = {},
                onError = {}
        )

    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

    fun changeBeaconsStatus(enable: Boolean, activity: Activity){
        if (enable)
            initBeaconsScanning(activity)
        else
            mObservationHandler?.stop()
    }
}