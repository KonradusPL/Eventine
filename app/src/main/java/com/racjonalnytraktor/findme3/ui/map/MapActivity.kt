package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import android.os.Bundle
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.utils.MapHelper
import kotlinx.android.synthetic.main.activity_map.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast
import org.greenrobot.eventbus.EventBus




class MapActivity : BaseActivity(),MapMvp.View, MapHelper.MapClickListener {

    private lateinit var mMapHelper: MapHelper
    private lateinit var mPresenter: MapPresenter<MapMvp.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mMapHelper = MapHelper(this,null)

        (viewMap as SupportMapFragment).getMapAsync(mMapHelper)

        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdate(location: LocationEvent) {
        toast(location.lat.toString())
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        mPresenter = MapPresenter()
        mPresenter.onAttach(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        mPresenter.onDetach()
    }

    override fun onMapClick(location: Location) {
    }

    override fun onMarkerClick(marker: Marker) {
    }

}
