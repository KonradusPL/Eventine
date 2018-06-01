package com.racjonalnytraktor.findme3.ui.map

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.design.widget.TabLayout
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.CreatePingBasicFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.CreatePingDetailsFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.ManagementFragment
import com.racjonalnytraktor.findme3.utils.MapHelper
import kotlinx.android.synthetic.main.activity_map.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus



class MapActivity : BaseActivity(),MapMvp.View, MapHelper.MapClickListener {

    private lateinit var mMapHelper: MapHelper
    lateinit var mPresenter: MapPresenter<MapMvp.View>

    lateinit var fragmentMap: SupportMapFragment
    lateinit var fragmentManagement: ManagementFragment
    lateinit var fragmentCreatePingBasic: CreatePingBasicFragment<MapMvp.View>
    lateinit var fragmentCreatePingDetails: CreatePingDetailsFragment<MapMvp.View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mPresenter = MapPresenter()
        mPresenter.onAttach(this)

        mMapHelper = MapHelper(this,null)

        fragmentMap = SupportMapFragment.newInstance()
        fragmentManagement = ManagementFragment()
        fragmentCreatePingBasic = CreatePingBasicFragment()
        fragmentCreatePingDetails = CreatePingDetailsFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragmentMap)
                .replace(R.id.containerCreatePing,fragmentCreatePingBasic)
                .commit()


        initTabs()

        fragmentMap.getMapAsync(mMapHelper)

        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initTabs(){
        tabLayoutMap.addOnTabSelectedListener(object:  TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.icon!!.setTint(ContextCompat.getColor(this@MapActivity,R.color.black))

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //if(tab!!.position != 1){
                 //   tabLayoutMap.getTabAt(1)!!.icon!!.setTint(ContextCompat.getColor(this@MapActivity,R.color.colorPrimary)) }
                tab?.icon!!.setTint(ContextCompat.getColor(this@MapActivity,R.color.colorPrimary))
                val fragment: Fragment
                when(tab.position){
                    2 -> fragment = fragmentMap
                    else -> fragment = fragmentManagement
                }
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit()
            }

        })
        tabLayoutMap.getTabAt(2)!!.select()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdate(location: LocationEvent) {
    }

    override fun changeCreateGroupFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerCreatePing,fragmentCreatePingDetails)
                .commit()
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
        tabLayoutMap.let {
            it.getTabAt(it.selectedTabPosition)?.icon?.setTint(ContextCompat.getColor(this@MapActivity,R.color.black))
        }
    }

    override fun onMapClick(location: Location) {
    }

    override fun onMarkerClick(marker: Marker) {
    }

    override fun onLongClickListener(location: LatLng) {
        slidingPing.openLayer(true)
    }

}
