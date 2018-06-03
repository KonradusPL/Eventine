package com.racjonalnytraktor.findme3.ui.map

import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.racjonalnytraktor.findme3.ui.manage.ManageSubGroupsActivity
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.CreatePingBasicFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.CreatePingDetailsFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.HistoryFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.ManagementFragment
import com.racjonalnytraktor.findme3.utils.MapHelper
import kotlinx.android.synthetic.main.activity_map.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapActivity : BaseActivity(),MapMvp.View, MapHelper.MapListener {

    private lateinit var mMapHelper: MapHelper
    lateinit var mPresenter: MapPresenter<MapMvp.View>

    lateinit var fragmentMap: SupportMapFragment
    lateinit var fragmentManagement: ManagementFragment
    lateinit var fragmentCreatePingBasic: CreatePingBasicFragment<MapMvp.View>
    lateinit var fragmentCreatePingDetails: CreatePingDetailsFragment<MapMvp.View>
    lateinit var fragmentHistory: HistoryFragment<MapMvp.View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fragmentMap = SupportMapFragment.newInstance()
        fragmentManagement = ManagementFragment()
        fragmentCreatePingBasic = CreatePingBasicFragment()
        fragmentCreatePingDetails = CreatePingDetailsFragment()
        fragmentHistory = HistoryFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragmentMap)
                .commit()

        mPresenter = MapPresenter()
        mPresenter.onAttach(this)

        mMapHelper = MapHelper(this,null)

        fragmentCreatePingBasic.mPresenter = mPresenter
        fragmentCreatePingDetails.mPresenter = mPresenter

        initTabs()

        listenSlidingState()
        fragmentMap.getMapAsync(mMapHelper)

        setSupportActionBar(toolbarMap)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun listenSlidingState() {
        var isSliderClosed: Boolean = slidingPing.isClosed
        Log.d("isClosed", isSliderClosed.toString())
        doAsync {
            while (!isDestroyed) {
                Thread.sleep(100)
                if(!isSliderClosed && isSliderClosed != slidingPing.isClosed){
                    uiThread {
                        Log.d("yuyu","yuyu")
                        mPresenter.clearData()
                        if(fragmentCreatePingDetails.isInLayout){
                            Log.d("vvv","vvv")
                            fragmentCreatePingDetails.clearData()

                        }

                    }

                }
                isSliderClosed = slidingPing.isClosed
            }
        }
    }

    private fun initTabs(){
        tabLayoutMap.addOnTabSelectedListener(object:  TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

                val fragment: Fragment
                when(tab!!.position){
                    2 -> openManageActivity()
                    1 -> mPresenter.onInfoTabClick()
                    0 -> mPresenter.onHistoryButtonClick()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab!!.icon!!.setTint(ContextCompat.getColor(this@MapActivity,R.color.black))

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("method","onTabSelected")
                val fragment: Fragment
                when(tab!!.position){
                    2 -> openManageActivity()
                    1 -> mPresenter.onInfoTabClick()
                    0 -> mPresenter.onHistoryButtonClick()
                }
            }
        })
        //tabLayoutMap.getTabAt(2)!!.select()
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
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        mPresenter.onDetach()
        tabLayoutMap.let {
            it.getTabAt(it.selectedTabPosition)?.icon?.setTint(ContextCompat.getColor(this@MapActivity,R.color.black))
        }
    }

    override fun updateSubGroups(item: String) {
        fragmentCreatePingDetails.updateList(item)
    }

    override fun onMapClick(location: Location) {
    }

    override fun onMarkerClick(marker: Marker) {
        val dialog = AlertDialog.Builder(this)
                .setMessage("Ustawić ping jako wykonany ?")
                .setPositiveButton("TAK",{_, i ->
                    marker.remove()
                }).setNegativeButton("NIE",{dialogInterface, i ->

                }).create()
        dialog.show()
    }

    override fun onLongClickListener(location: LatLng) {
        mPresenter.onMapLongClick(location)
    }

    override fun showCreatePingView(type: String) {
       slidingPing.openLayer(true)
        fragmentCreatePingDetails.type = type
        fragmentCreatePingBasic.type = type
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerCreatePing,fragmentCreatePingBasic)
                .commit()
        if(fragmentCreatePingBasic.isAdded){
            Log.d("kikiki","kikiki")
            if(type == "ping")
                fragmentCreatePingBasic.clearData()
            else
                fragmentCreatePingBasic.onInfo()
        }
    }

    override fun hideCreatePingView() {
        slidingPing.closeLayer(true)
    }

    override fun updatePings(ping: Ping) {
        mMapHelper.addPing(ping)
    }

    override fun getPresenter(): MapPresenter<MapMvp.View> {
        return mPresenter
    }

    override fun onMapPrepared() {
        mPresenter.onMapPrepared()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if(slidingPing.isClosed)
            return

        val checked = ArrayList<String>()

        var state = "basic"

        if (fragmentCreatePingDetails.isAdded){
            outState!!.putString("isAdded","true")
            checked.addAll(fragmentCreatePingDetails.mListAdapter.getCheckedGroups())
            state = "extended"
        }
        val bundle: Bundle

        if(fragmentCreatePingBasic.isInLayout)
            bundle = fragmentCreatePingBasic.getData()
        else bundle = Bundle()


        mPresenter.onSavingState(checked,bundle.getString("fieldTask").orEmpty(),
                bundle.getString("fieldDescr").orEmpty(),state)

    }

    override fun updateWithSavedData(task: String, descr: String, checked: List<String>, type: String, state: String) {
        Log.d("uiuiui",slidingPing.isClosed.toString())
        Log.d("uiuiui",slidingPing.isOpened.toString())

        if(type == "info")
            fragmentCreatePingBasic.type = type

        if(state == "extended"){
            changeCreateGroupFragment()
        }

        fragmentCreatePingBasic.updateData(task,descr)
        mPresenter.getAllSubGroups()
    }

    override fun updateCheckedGroups(checked: List<String>) {
        fragmentCreatePingDetails.mListAdapter.updateCheckedGroups(checked)
    }

    override fun openManageActivity() {
        startActivity(Intent(this, ManageSubGroupsActivity::class.java))
    }

    override fun openHistoryFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerCreatePing,fragmentHistory)
                .commit()
        slidingPing.openLayer(true)
    }
}
