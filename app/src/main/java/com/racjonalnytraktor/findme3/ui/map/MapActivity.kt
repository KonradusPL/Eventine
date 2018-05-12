package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.utils.MapHelper
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : BaseActivity(), MapHelper.MapClickListener {

    private lateinit var mapHelper: MapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapHelper = MapHelper(this,null)

        (viewMap as SupportMapFragment).getMapAsync(mapHelper)

        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapClick(location: Location) {
    }

    override fun onMarkerClick(marker: Marker) {
    }

}
