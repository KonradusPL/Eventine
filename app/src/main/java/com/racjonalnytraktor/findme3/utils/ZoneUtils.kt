package com.racjonalnytraktor.findme3.utils

import android.location.Location
import android.util.Log
import com.estimote.proximity_sdk.api.ProximityZone
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.data.model.map.Zone

object ZoneUtils {

    interface BeaconListener{
        fun onEnterZone(tag: String)
    }
    private val defaultRange = 1.0
    val beaconZones = ArrayList<ProximityZone>()
    var listener: BeaconListener? = null
    val zonesString = arrayListOf("Kanciapa","Wyjście poziom -1","Wejście główne",
            "Chill zone","Pokój organizatora","Aula spadochronowa","Wyjście poziom 0","Jajko",
            "Foto","Ksero","Sala 105","Aula B")
    val zonesLocation = arrayListOf(
            LatLng(52.208393, 21.00929),
            LatLng(52.208447, 21.009369),
            LatLng(52.20875, 21.008202),
            LatLng(52.208992, 21.00842),
            LatLng(52.208925, 21.008399),
            LatLng(52.208764, 21.009228),
            LatLng(52.208853, 21.008729),
            LatLng(52.20893, 21.009022),
            LatLng(52.209277, 21.009093),
            LatLng(52.209335, 21.008414),
            LatLng(52.209156, 21.00829),
            LatLng(52.208817, 21.00833))
    val zonesFloors = arrayListOf(-1,-1,0,0,0,0,0,0,1,1,1,1)

    val zones = arrayListOf(
            Zone("Kanciapa",-1,LatLng(52.208492, 21.009207)),
            Zone("Wyjście poziom -1",-1,LatLng(52.20839, 21.009285)),
            Zone("Wejście główne",0,LatLng(52.20875, 21.008202)),
            Zone("Chill zone",0,LatLng(52.20904, 21.008396)),
            Zone("Pokój organizatora",0,LatLng(52.209045, 21.00868)),
            Zone("Aula spadochronowa",0,LatLng(52.208764, 21.008764)),
            Zone("Wyjście poziom 0",0,LatLng(52.20931, 21.008797)),
            Zone("Jajko",0,LatLng(52.20893, 21.009056)),
            Zone("Foto",1,LatLng(52.208717, 21.008696)),
            Zone("Ksero",1,LatLng(52.208534, 21.00917)),
            Zone("Sala 105",1,LatLng(52.209156, 21.00829)),
            Zone("Aula B",1,LatLng(52.2089, 21.008389)))

    init {
        for(zone in zones){
            Log.d("initbeacons",zone.name)
            beaconZones.add(ProximityZoneBuilder()
                    .forTag(zone.name)
                    .inCustomRange(8.0)
                    .onEnter {
                        Log.d("Beacons","Enter")
                        listener?.onEnterZone(zone.name)
                    }
                    .build())
        }
    }



}