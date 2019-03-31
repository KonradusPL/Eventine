package com.racjonalnytraktor.findme3.data.model.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

data class ZoneOnMap(var floor: Int,
                     var usersCount: Int = 0,
                     var zoneName: String = "",
                     var marker: Marker,
                     var latLng: LatLng)