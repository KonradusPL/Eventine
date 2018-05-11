package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.location.Location
import android.support.v4.app.Fragment
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.racjonalnytraktor.findme3.R


class MapHelper(val context: Context, fragment: Fragment?) : OnMapReadyCallback {

    private var clickListener: MapClickListener

    private lateinit var mMap: GoogleMap

    private val markersList = ArrayList<Marker>()

    interface MapClickListener {
        fun onMapClick(location: Location)
        fun onMarkerClick(marker: Marker)
    }

    init {
        clickListener = context as MapClickListener
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("map","ready")
        mMap = googleMap

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.theme_map))

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        /*mMap.setOnMapClickListener { latLng ->
            val location = Location("GPS")
            location.setLongitude(latLng.longitude)
            location.setLatitude(latLng.latitude)
            clickListener.onMapClick(location)
        }
        mMap.setOnMarkerClickListener { marker ->
            clickListener.onMarkerClick(marker)
            true
        }*/


    }

    fun addMarker(lat: Double, lng: Double, tag: Int) {
        val marker = mMap.addMarker(MarkerOptions().position(LatLng(lat, lng)))
        marker.tag = tag
        markersList.add(marker)
    }

    fun setMapPosition(lat: Double, lng: Double) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
    }

    fun zoom() {
        this.mMap.animateCamera(CameraUpdateFactory.zoomBy(15f))
    }

    fun removeAllMarkers() {
        for (marker in markersList) {
            marker.remove()
        }
        markersList.clear()
    }
}