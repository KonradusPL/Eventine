package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.location.Location
import android.support.v4.app.Fragment
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.PersonOnMap
import com.racjonalnytraktor.findme3.data.model.PingOnMap
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapHelper(val context: Context, fragment: Fragment?) : OnMapReadyCallback {

    var isUserInitialized = false

    private var clickListener: MapClickListener

    private lateinit var mMap: GoogleMap

    private lateinit var userOnMap: PersonOnMap
    private val peopleOnMap = ArrayList<PersonOnMap>()
    val pingsOnMap = ArrayList<PingOnMap>()

    interface MapClickListener {
        fun onMapClick(location: Location)
        fun onMarkerClick(marker: Marker)
        fun onLongClickListener(location: LatLng)
    }

    init {
        clickListener = context as MapClickListener
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("map","ready")
        mMap = googleMap
        mMap.setPadding(0,60,0,0)

       // moveCamera(LatLng(51.101809,22.854009))

        //mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.theme_map))

        mMap.setOnMapClickListener { latLng ->
            val location = Location("GPS")
            /*location.longitude = latLng.longitude
            location.latitude = latLng.latitude
            addFriendToMap(PersonOnMap("Marcin Michno","",latLng.latitude,latLng.longitude,null))
            clickListener.onMapClick(location)*/
        }
        mMap.setOnMarkerClickListener { marker ->
            clickListener.onMarkerClick(marker)
            true
        }
        mMap.setOnMapLongClickListener { latLng ->
            clickListener.onLongClickListener(latLng)
        }
    }

    fun moveCamera(position: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position))
    }

    fun addUserToMap(person: PersonOnMap){
        isUserInitialized = true

        val marker = mMap.addMarker(MarkerOptions().position(LatLng(person.firstLat,person.firstLng)))
        doAsync {
            val bitmapMarker = ImageHelper.getMarkerImage(context,R.color.colorPrimaryDark)

            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapMarker)
            uiThread {
                marker.setIcon(bitmapDescriptor)
                person.marker = marker
                userOnMap = person
            }
        }
    }

    fun addFriendToMap(person: PersonOnMap){
        val marker = mMap.addMarker(MarkerOptions().position(LatLng(person.firstLat,person.firstLng)))
        doAsync {
            val bitmapMarker = ImageHelper.getMarkerImage(context,R.color.colorPrimary)

            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapMarker)
            uiThread {
                marker.setIcon(bitmapDescriptor)
                person.marker = marker
                peopleOnMap.add(person)
            }

        }

    }

    fun addPing(ping: Ping){
        val marker = mMap.addMarker(MarkerOptions().
                position(LatLng(ping.geo[0],ping.geo[2])))

        marker.tag = ping.title
        marker.title = ping.title

        val pingOnMap = PingOnMap()
        pingOnMap.marker = marker
        pingOnMap.ping = ping

    }

    fun addPingToMap(ping: PingOnMap){
        //ping.marker =
    }

    fun setUserLocation(location: LatLng){
        userOnMap.marker?.position = location
        moveCamera(location)
    }

    fun setPersonLocation(location: LatLng,fullName: String){
        for (person in peopleOnMap){
            if (person.fullName == fullName){
                person.marker?.position = location
            }
        }
    }

    fun zoom() {
        this.mMap.animateCamera(CameraUpdateFactory.zoomBy(15f))
    }


}