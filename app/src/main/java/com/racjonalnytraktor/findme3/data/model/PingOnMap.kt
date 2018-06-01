package com.racjonalnytraktor.findme3.data.model

import com.google.android.gms.maps.model.Marker
//1.twórca, latlng, tytul, opis, grupy docelowe,ile osób, na kiedy i kto wykonał

class PingOnMap {
    var marker: Marker? = null
    var creator: String = ""
    var location: String = ""
    var title: String = ""

}