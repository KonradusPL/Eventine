package com.racjonalnytraktor.findme3.utils

import android.content.res.Resources
import android.graphics.Bitmap
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.racjonalnytraktor.findme3.ui.map.MapMvp


class MapHelper(val mvpView: MapMvp.View, fragment: Fragment?) : OnMapReadyCallback {

    var isUserInitialized = false

    private var listenerPresenter: MapListener = mvpView.getPresenter()
    private var listenerView : MapViewListener = mvpView

    private lateinit var mMap: GoogleMap

    private lateinit var userOnMap: PersonOnMap
    private val peopleOnMap = ArrayList<PersonOnMap>()
    val pingsOnMap = ArrayList<PingOnMap>()

    interface MapListener {
        fun onMapClick(location: Location)
        fun onMarkerClick(ping: Ping)
        fun onLongClickListener(location: LatLng)
        fun onMapPrepared()
    }

    interface MapViewListener{
        fun updateMapImage(bitmap: Bitmap)
    }

    fun getImage(){
        mMap.snapshot{ bitmap ->
            listenerView.updateMapImage(bitmap)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("map","ready")
        mMap = googleMap

        listenerPresenter.onMapPrepared()

        mMap.setPadding(0,60,0,0)

        val cameraPosition = CameraPosition.Builder()
                .target(LatLng(51.101669, 22.854012))
                .zoom(18f)
                .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),1,null)

        try {
            val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            mvpView.getCtx(), R.raw.theme_map))
        }catch (e: Resources.NotFoundException) {
            Log.e("asdqwe", "Can't find style. Error: ", e)
        }
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mvpView.getCtx(), R.raw.theme_map))

        mMap.setOnMapClickListener { latLng ->
            val location = Location("GPS")
            location.longitude = latLng.longitude
            location.latitude = latLng.latitude
            listenerPresenter.onMapClick(location)
        }
        mMap.setOnMarkerClickListener { marker ->
            for(ping in pingsOnMap){
                if(ping.marker.position == marker.position){
                    Log.d("pongaponga",ping.ping.inProgress.toString())
                    listenerPresenter.onMarkerClick(ping.ping)
                    //marker.showInfoWindow()
                    break
                }
            }
            true
        }
        mMap.setOnMapLongClickListener { latLng ->
            listenerPresenter.onLongClickListener(latLng)
        }
    }

    fun removeMarker(position: LatLng){
        for(item in pingsOnMap){
            if(item.ping!!.geo[0] == position.latitude){
                item.marker?.remove()
                pingsOnMap.remove(item)
                break
            }
        }
    }

    fun moveCamera(position: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(position),2000,null)
    }

    fun zoomCamera(value: Float = 15f){
        mMap.animateCamera(CameraUpdateFactory.zoomTo(value),2000,null)
    }

    fun addUserToMap(person: PersonOnMap){
        isUserInitialized = true

        val marker = mMap.addMarker(MarkerOptions().position(LatLng(person.firstLat,person.firstLng)))
        doAsync {
            val bitmapMarker = ImageHelper.getMarkerImage(mvpView.getCtx(),R.color.colorPrimaryDark)

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
            val bitmapMarker = ImageHelper.getMarkerImage(mvpView.getCtx(),R.color.colorPrimary)

            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapMarker)
            uiThread {
                marker.setIcon(bitmapDescriptor)
                person.marker = marker
                peopleOnMap.add(person)
            }

        }

    }

    fun addPing(ping: Ping,animation: Boolean){
        Log.d("ping",ping.title)
        Log.d("ping",ping.creator)
        Log.d("ping",ping.desc)
        Log.d("ping",ping.geo.toString())
        Log.d("creatorName",ping.creatorName)
        Log.d("asdqweasd","asdqweasd")
       //val bitmapMarker = ImageHelper.getPingMarkerBitmap(context,R.color.colorPrimary)
        //val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapMarker)

        //if (ping.geo.size<2)
            //return

        Log.d("positionOfMarker1",ping.geo[0].toString())
        Log.d("positionOfMarker2",ping.geo[1].toString())
        Log.d("titleOfMarker",ping.title)

        val marker = mMap.addMarker(MarkerOptions()
                .position(LatLng(ping.geo[0],ping.geo[1]))
                .title(ping.title))

        //if(ping.inProgress)
           // marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

        Log.d("kuku","kukuku")

        val newPing = Ping()
        newPing.clone(ping)

        val pingOnMap = PingOnMap(newPing, marker)
        pingsOnMap.add(pingOnMap)

    }

    fun updatePing(updatedPing: Ping){
        var isPingNew = true
        for(ping in pingsOnMap){
            if(ping.ping.pingId == updatedPing.pingId){
                ping.marker.position = LatLng(updatedPing.geo[0],updatedPing.geo[1])
                isPingNew = false
            }
        }
        if(isPingNew){
            addPing(updatedPing,false)
        }
    }

    fun updatePings(newPings: List<Ping>){
        Log.d("michno1",newPings.size.toString())
        val oldPings = ArrayList<String>()

        for(_ping in pingsOnMap){
            oldPings.add(_ping.ping.pingId)
        }

        for (newPing in newPings){
            var isPingNew = true

            for(ping in pingsOnMap){
                if(ping.ping.pingId == newPing.pingId){
                    oldPings.remove(ping.ping.pingId)
                    Log.d("iopiop","iopiop")
                    isPingNew = false
                    if(newPing.ended){
                        ping.marker.remove()
                        pingsOnMap.remove(ping)
                        break
                    }else if(newPing.inProgress){
                        ping.ping.inProgress = true
                        ping.marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                         break
                    }
                }
            }
            Log.d("michno2",newPings.size.toString())

            if (isPingNew && !newPing.ended)
                addPing(newPing,false)
        }

        for (_ping in oldPings){
            for (ping in pingsOnMap){
                if(ping.ping.pingId == _ping){
                    ping.marker.remove()
                    pingsOnMap.remove(ping)
                    break
                }
            }
        }
    }

    fun clearPings(){
        for(ping in pingsOnMap){
            ping.marker.remove()
        }
        pingsOnMap.clear()
    }

    fun removePing(pingId: String){
        for(ping in pingsOnMap){
            if(ping.ping.pingId == pingId){
                pingsOnMap.remove(ping)
                ping.marker.remove()
                break
            }
        }
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



}