package com.racjonalnytraktor.findme3.data.model

import com.google.android.gms.maps.model.Marker
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping

//1.twórca, latlng, tytul, opis, grupy docelowe,ile osób, na kiedy i kto wykonał

data class PingOnMap(val ping: Ping, val marker: Marker)