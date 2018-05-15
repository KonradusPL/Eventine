package com.racjonalnytraktor.findme3.data.model.event_bus

import com.google.android.gms.maps.model.LatLng

data class LocationEvent(val lat: Double, val lng: Double, val acc: Double)