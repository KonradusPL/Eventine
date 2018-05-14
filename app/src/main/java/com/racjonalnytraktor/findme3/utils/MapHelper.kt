package com.racjonalnytraktor.findme3.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.location.Location
import android.support.v4.app.Fragment
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.PersonOnMap
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapHelper(val context: Context, fragment: Fragment?) : OnMapReadyCallback {


    private var clickListener: MapClickListener

    private lateinit var mMap: GoogleMap

    private val markersList = ArrayList<Marker>()
    private val peopleOnMap = ArrayList<PersonOnMap>()

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
        mMap.setPadding(0,60,0,0)

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.theme_map))

        mMap.setOnMapClickListener { latLng ->
            val location = Location("GPS")
            location.longitude = latLng.longitude
            location.latitude = latLng.latitude
            addPersonToMap(PersonOnMap("Marcin Michno","",latLng.latitude,latLng.longitude,null))
            clickListener.onMapClick(location)
        }
        mMap.setOnMarkerClickListener { marker ->
            clickListener.onMarkerClick(marker)
            true
        }
    }

    fun setMapPosition(lat: Double, lng: Double) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
    }

    fun addPersonToMap(person: PersonOnMap){
        val marker = mMap.addMarker(MarkerOptions().position(LatLng(person.firstLat,person.firstLng)))
        doAsync {
            val bitmapProfile = Picasso.get()
                    .load("https://i2.wp.com/startupkids.pl/wp-content/uploads/2018/01/kuba-mularski-250x343-supervisor.jpg?fit=250%2C343")
                    .resize(100,100)
                    .transform(CircleTransform())
                    .get()

            val bitmapMarkerFromRes = BitmapFactory.decodeResource(context.resources,R.drawable.marker_icon)

            val bitmapOld = ImageHelper.getResizedBitmap(bitmapMarkerFromRes,120,120)

            val bitmapNew = Bitmap.createBitmap(bitmapOld.width,bitmapOld.height,Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmapNew)
            val paint = Paint()

            canvas.drawBitmap(bitmapOld,0f,0f,paint)
            canvas.drawBitmap(bitmapProfile,0f,0f,paint)

            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapNew)
            uiThread {
                marker.setIcon(bitmapDescriptor)
                person.marker = marker
                peopleOnMap.add(person)
            }

        }

    }
    // mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))

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

    fun removeAllMarkers() {
        for (marker in markersList) {
            marker.remove()
        }
        markersList.clear()
    }
}