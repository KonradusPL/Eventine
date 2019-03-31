package com.racjonalnytraktor.findme3.data.model.map

import com.google.android.gms.maps.model.Marker

data class PersonOnMap(var fullName: String,
                       var profileUri: String,
                       var firstLat: Double,
                       var firstLng: Double,
                       var marker: Marker?)