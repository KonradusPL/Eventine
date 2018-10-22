package com.racjonalnytraktor.findme3.utils

import android.util.Log
import com.estimote.proximity_sdk.api.ProximityZone
import com.estimote.proximity_sdk.api.ProximityZoneBuilder

object BeaconUtils {

    interface BeaconListener{
        fun onEnterZone(tag: String)
    }
    private val defaultRange = 1.0
    val beaconZones = ArrayList<ProximityZone>()
    var listener: BeaconListener? = null
    private val zonesString = arrayListOf("Kanciapa","Wyjście poziom -1","Wejście główne",
            "Chill zone","Pokój organizatora","Aula spadochronowa","Wyjście poziom 0","Jajko",
            "Foto","Ksero","Sala 105","Aula B")

    init {
        for(zone in zonesString){
            beaconZones.add(ProximityZoneBuilder()
                    .forTag(zone)
                    .inCustomRange(10.0)
                    .onEnter {
                        Log.d("Beacons","Enter")
                        listener?.onEnterZone(zone)
                    }
                    .build())
        }
    }



}