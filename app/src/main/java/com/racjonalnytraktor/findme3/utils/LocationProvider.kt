package com.racjonalnytraktor.findme3.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import io.reactivex.Observable
import org.jetbrains.anko.toast
import java.util.*


class LocationProvider(private val millis: Long, private val context: Context){

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val REQUEST_CHECK_SETTINGS = 10

    var currentLocation = Location("GPS")

    fun init() {
        locationRequest = LocationRequest().apply {
            interval = millis
            fastestInterval = millis
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    currentLocation.set(location)
                    context.toast("blabla")
                }
            }
        }

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)

        val settingsClient = LocationServices.getSettingsClient(context)
        val taskCheckSettings = settingsClient.checkLocationSettings(builder.build())



        taskCheckSettings.addOnSuccessListener{
            Log.d("Michno", "OnSuccessListener")
            startUpdatingLocation()

        }

        taskCheckSettings.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                Log.d("Michno","OnFailureListener")
                //startLocationUpdate()
                try {
                    exception.startResolutionForResult(context as Activity, REQUEST_CHECK_SETTINGS)
                    startUpdatingLocation()
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startUpdatingLocation() {
        Log.d("asdasd","lkjlkj")
        locationClient.requestLocationUpdates(locationRequest,
                locationCallback,null)
    }
}