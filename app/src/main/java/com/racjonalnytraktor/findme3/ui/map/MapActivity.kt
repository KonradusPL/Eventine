package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.ManagementFragment
import com.racjonalnytraktor.findme3.utils.MapHelper
import kotlinx.android.synthetic.main.activity_map.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast
import org.greenrobot.eventbus.EventBus


class MapActivity : BaseActivity(),MapMvp.View, MapHelper.MapClickListener {

    private lateinit var mMapHelper: MapHelper
    private lateinit var mPresenter: MapPresenter<MapMvp.View>

    lateinit var fragmentMap: SupportMapFragment
    lateinit var fragmentManagement: ManagementFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mMapHelper = MapHelper(this,null)

        fragmentMap = SupportMapFragment.newInstance()
        fragmentManagement = ManagementFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragmentMap)
                .commit()

        fragmentMap.getMapAsync(mMapHelper)

        initTabs()

        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initTabs(){
        /*tabLayoutMap.addOnTabSelectedListener(object:  TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.icon!!.setTint(getColor(R.color.black))

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!!.position != 1){
                    tabLayoutMap.getTabAt(1)!!.icon!!.setTint(getColor(R.color.black))
                }
                Log.d("zxczxc",tab!!.position.toString())
                tab.icon!!.setTint(getColor(R.color.colorPrimary))
                val fragment: Fragment
                when(tab.position){
                    1 -> fragment = fragmentMap
                    else -> fragment = fragmentManagement
                }
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit()
            }

        })
        tabLayoutMap.getTabAt(1)!!.icon!!.setTint(getColor(R.color.colorPrimary))*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdate(location: LocationEvent) {

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
