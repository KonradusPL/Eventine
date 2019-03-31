package com.racjonalnytraktor.findme3.data.model.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class Zone(var name: String, var floor: Int, var location: LatLng)