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
import com.racjonalnytraktor.findme3.data.model.map.PingOnMap
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.google.android.gms.maps.model.UrlTileProvider
import com.google.android.gms.maps.model.TileProvider
import java.net.MalformedURLException
import java.net.URL
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.TileOverlay
import com.racjonalnytraktor.findme3.data.model.map.ZoneOnMap
import com.racjonalnytraktor.findme3.data.model.map.ZoneUpdate


class MapHelper(val mvpView: MapMvp.View) : OnMapReadyCallback {

    private var listenerPresenter: MapListener = mvpView.getPresenter()
    private var listenerView : MapViewListener = mvpView

    private var mMap: GoogleMap? = null
    private var mTileOverlay: TileOverlay? = null

    val pingsOnMap = ArrayList<PingOnMap>()
    val zonesOnMap = ArrayList<ZoneOnMap>()
    private var mFloor = 0

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
        mMap?.snapshot{ bitmap ->
            listenerView.updateMapImage(bitmap)
        }
    }

    private var tileProvider: TileProvider = object : UrlTileProvider(256, 256) {
        override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {

            /* Define the URL pattern for the tile images */
            val s = String.format("http://35.234.124.12:3000/images/%d/%d/%d/file.png",
                    mFloor, x, y)
            /*val s = String.format("file:///android_asset/-1/292739_172683.png",
                    mFloor, x, y)*/

            if (!checkTileExists(x, y, zoom)) {
                return null
            }

            try {
                return URL(s)
            } catch (e: MalformedURLException) {
                throw AssertionError(e)
            }

        }

        private fun checkTileExists(x: Int, y: Int, zoom: Int): Boolean {
            var exist = false
            for(tile in MapUtils.tiles){
                if(tile.x == x && tile.y == y)
                    exist = true
            }
            Log.d("checkTileExists",(zoom == 19 && exist).toString())
            return zoom == 19 && exist
        }
    }


    fun onChangeFloor(floor: Int){
        mFloor = floor
        mTileOverlay?.clearTileCache()
        for (ping in pingsOnMap){
            ping.marker.isVisible = ping.ping.floor == mFloor
        }
        for(zone in zonesOnMap){
            zone.marker.isVisible = zone.floor == mFloor
        }
    }

    fun updateZones(zones: ArrayList<ZoneUpdate>){
        for (i in 0..zones.lastIndex){
            val count = if(zones[i].usersCount > 9) 9 else zones[i].usersCount
            zonesOnMap[i].usersCount = count
            zonesOnMap[i].marker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    ImageHelper.getZoneBitMap(mvpView.getCtx(),count,"")))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("map","ready")
        mMap = googleMap

        listenerPresenter.onMapPrepared()

        mMap?.apply {
            setPadding(0,60,0,0)
            isBuildingsEnabled = false
            uiSettings.isTiltGesturesEnabled = false
            setMinZoomPreference(19f)
            setMaxZoomPreference(19f)
        }

        val cameraPosition = CameraPosition.Builder()
                .target(LatLng(52.208856, 21.008713))
                .zoom(19f)
                .build()
        mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),1,null)

        try {
            mMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                            mvpView.getCtx(), R.raw.theme_map))
        }catch (e: Resources.NotFoundException) {
            Log.e("myErrors", "Can't find style. Error: ", e)
        }

        initZones()

        mTileOverlay = mMap?.addTileOverlay(TileOverlayOptions()
                .tileProvider(tileProvider))

        mMap?.setOnMapClickListener { latLng ->
            val location = Location("GPS")
            location.longitude = latLng.longitude
            location.latitude = latLng.latitude
            listenerPresenter.onMapClick(location)
        }
        mMap?.setOnMarkerClickListener { marker ->
            if(marker.tag == "zone")
                marker.showInfoWindow()

            for(ping in pingsOnMap){
                if(ping.marker.position == marker.position){
                    Log.d("pongaponga",ping.ping.inProgress.toString())
                    listenerPresenter.onMarkerClick(ping.ping)
                    break
                }
            }
            true
        }
        mMap?.setOnMapLongClickListener { latLng ->
            listenerPresenter.onLongClickListener(latLng)
        }
    }

    fun initZones(){
        if (mMap == null)
            return
        for (zone in ZoneUtils.zones){
            val marker = mMap?.addMarker(MarkerOptions()
                    .position(zone.location)
                    .title(zone.name)
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            ImageHelper.getZoneBitMap(mvpView.getCtx(),0,zone.name))))
            marker?.tag = "zone"
            if(zone.floor != mFloor)
                marker?.isVisible = false

            zonesOnMap.add(ZoneOnMap(zone.floor,
                            0,
                            zone.name,
                            marker!!,
                            zone.location))
        }
    }

    fun addPing(ping: Ping,animation: Boolean){
        if(mMap != null){
            val color = if(ping.inProgress) R.color.orange else R.color.colorPrimary
            val marker = mMap?.addMarker(MarkerOptions()
                    .position(LatLng(ping.geo[0],ping.geo[1]))
                    .title(ping.title)
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageHelper.getPingBitmap(mvpView.getCtx(),color))))
            val newPing = Ping()
            newPing.clone(ping)
            val pingOnMap = PingOnMap(newPing, marker!!)
            pingsOnMap.add(pingOnMap)
        }
    }

    fun updatePings(newPings: List<Ping>, floor: Int){
        Log.d("michno1",newPings.size.toString())
        mFloor = floor
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
                        Log.d("MapHelper","ping is done")
                        ping.marker.remove()
                        pingsOnMap.remove(ping)
                        break
                    }else if(newPing.inProgress){
                        ping.ping.inProgress = true
                        ping.marker.setIcon(
                                BitmapDescriptorFactory.fromBitmap(
                                        ImageHelper.getPingBitmap(mvpView.getCtx(),R.color.orange)))
                         break
                    }
                }
            }
            if (isPingNew && !newPing.ended)
                addPing(newPing,false)
        }
        Log.d("current floor ",mFloor.toString())

        for (ping in pingsOnMap){
            Log.d("pingsOnMap floor", ping.ping.floor.toString())
            ping.marker.isVisible = ping.ping.floor == mFloor
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

    fun addPingToMap(ping: PingOnMap){
        //ping.marker =
    }
}