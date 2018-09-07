package com.racjonalnytraktor.findme3.ui.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.DatePicker
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.sectionItem
import com.google.android.gms.maps.SupportMapFragment
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.racjonalnytraktor.findme3.ui.manage.ManageSubGroupsActivity
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.*
import com.racjonalnytraktor.findme3.utils.MapHelper
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.dialog_ping.view.*
import kotlinx.android.synthetic.main.dialog_time.view.*
import kotlinx.android.synthetic.main.item_tab.view.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/*Potrzebne fragmenty :
    przez alfe:
        opcje
        grupy
        profile
    przez slide:
        dodaj zadanie:


*/

class MapActivity : BaseActivity(),MapMvp.View{

    private lateinit var mMapHelper: MapHelper
    lateinit var mPresenter: MapPresenter<MapMvp.View>

    lateinit var fragmentMap: SupportMapFragment
    lateinit var fragmentManagement: ManagementFragment
    lateinit var fragmentCreatePingBasic: CreatePingBasicFragment<MapMvp.View>
    lateinit var fragmentCreatePingDetails: CreatePingDetailsFragment<MapMvp.View>
    lateinit var fragmentHistory: HistoryFragment<MapMvp.View>
    lateinit var fragmentOptions: SettingsFragment

    lateinit var drawerMap: Drawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        Log.d("lifecycler","onCreate")

        mPresenter = MapPresenter()

        fragmentMap = SupportMapFragment.newInstance()
        fragmentManagement = ManagementFragment()
        fragmentCreatePingBasic = CreatePingBasicFragment()
        fragmentCreatePingDetails = CreatePingDetailsFragment()
        fragmentHistory = HistoryFragment()
        fragmentOptions = SettingsFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer,fragmentMap)
                .commit()

        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragmentCreatePingBasic)
                .addToBackStack(null)
                .commit()

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragmentOptions)
                .commit()

        mMapHelper = MapHelper(this,null)

        fragmentCreatePingBasic.mPresenter = mPresenter
        fragmentCreatePingDetails.mPresenter = mPresenter

        initTabs()

        mPresenter.onAttach(this)

        listenSlidingState()
        fragmentMap.getMapAsync(mMapHelper)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycler","onResume")
    }

    private fun listenSlidingState() {
        var isSliderClosed: Boolean = slidePanel.isClosed
        Log.d("isClosed", isSliderClosed.toString())
        doAsync {
            while (!isDestroyed) {
                Thread.sleep(100)
                if(!isSliderClosed && isSliderClosed != slidePanel.isClosed){
                    uiThread {
                        Log.d("yuyu","yuyu")
                        if(fragmentCreatePingBasic.isAdded)
                            fragmentCreatePingBasic.clearData()
                        mPresenter.clearData()
                        if(fragmentCreatePingDetails.isInLayout){
                            Log.d("vvv","vvv")
                            fragmentCreatePingDetails.clearData()

                        }

                    }

                }
                isSliderClosed = slidePanel.isClosed
            }
        }
    }

    override fun setUpLeftNavigation(groups: ArrayList<Group>) {
        drawerMap = drawer {

            gravity = GravityCompat.END
            displayBelowStatusBar = false

            headerView = LayoutInflater.from(this@MapActivity).inflate(R.layout.navigation_header,null)
            primaryItem("Wyloguj się"){
                icon = R.drawable.ic_directions_run_black_24dp
                tag = "logout"
            }
            sectionItem("Zmień wydarzenie") {
                selectable = false
            }
        }
        drawerMap.deselect()
        for(group in groups)
        drawerMap.addItem(PrimaryDrawerItem()
                .withName(group.groupName)
                .withTag(group.groupName))

        drawerMap.setOnDrawerItemClickListener { view, position, drawerItem ->
            if(drawerItem.tag is String){
                if(drawerItem.tag == "logout")
                    mPresenter.onLogoutButtonClick()
                else
                    mPresenter.onChangeGroupClick(drawerItem.tag.toString())

            }
            return@setOnDrawerItemClickListener true
        }
    }

    private fun initTabs(){
        val icons = arrayListOf<IIcon>(
                FontAwesome.Icon.faw_cog,
                FontAwesome.Icon.faw_history,
                FontAwesome.Icon.faw_users,
                FontAwesome.Icon.faw_users,
                FontAwesome.Icon.faw_map_marker_alt)
        val titles = arrayListOf("Opcje","Historia","nic","Grupy","Mapa")

        for(i in 0..4){
            val tabView = LayoutInflater.from(this).inflate(R.layout.item_tab,null,false)
            if(i != 2){
                tabView.icon.icon = IconicsDrawable(this)
                        .icon(icons[i])
                        .color(ContextCompat.getColor(this,R.color.greyTab))
                        .sizeDp(22)
                tabView.text.text = titles[i]
            }else{
                tabView.isClickable = false
            }
            tabLayoutMap.addTab(tabLayoutMap.newTab().setCustomView(tabView))
        }
        tabLayoutMap.addOnTabSelectedListener(object:  TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val fragment: Fragment
                when(tab!!.position){
                    2 -> openManageActivity()
                    1 -> showSlide(fragmentHistory)
                    0 -> replaceFragment(fragmentOptions,R.id.fragmentContainer)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { //tab!!.icon!!.setTint(ContextCompat.getColor(this@MapActivity,R.color.black))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("method","onTabSelected")
                val fragment: Fragment
                when(tab!!.position){
                    2 -> openManageActivity()
                    1 -> showSlide(fragmentHistory)
                    0 -> replaceFragment(fragmentOptions,R.id.fragmentContainer)
                }
            }
        })
        iconCircle.icon = IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_plus)
                .sizeDp(30)
                .color(Color.WHITE)
        //tabLayoutMap.getTabAt(2)!!.select()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdate(location: LocationEvent) {
    }

    override fun changeCreateGroupFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragmentCreatePingDetails)
                .commit()
        fragmentCreatePingBasic.clearFields()
    }

    override fun onStart() {
        super.onStart()
        Log.d("lifecycler","onStart")
        mPresenter.isAttached = true
        EventBus.getDefault().register(this)
        mPresenter.startUpdatingPings()
    }

    override fun onStop() {
        super.onStop()
        Log.d("lifecycler","onStop")
        EventBus.getDefault().unregister(this)
        mPresenter.isAttached = false
        tabLayoutMap.let {
           // it.getTabAt(it.selectedTabPosition)?.icon?.setTint(ContextCompat.getColor(this@MapActivity,R.color.black))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDetach()
    }

    override fun updateSubGroups(item: String) {
        fragmentCreatePingDetails.updateList(item)
    }

    override fun showCreatePingView(type: String) {
        fragmentCreatePingDetails.type = type
        fragmentCreatePingBasic.type = type
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragmentCreatePingBasic)
                .commit()
        slidePanel.openLayer(true)
        if(fragmentCreatePingBasic.isAdded){
            Log.d("kikiki","kikiki")
            if(type == "ping")
                fragmentCreatePingBasic.clearData()
            else
                fragmentCreatePingBasic.onInfo()
        }
    }

    override fun hideCreatePingView() {
        if(fragmentCreatePingBasic.isAdded){
            fragmentCreatePingBasic.clearData()
        }
        slidePanel.closeLayer(true)
    }

    override fun addPing(ping: Ping) {
        mMapHelper.addPing(ping,true)
    }

    override fun updatePings(pings: List<Ping>,value: Boolean) {
        mMapHelper.updatePings(pings)
    }

    override fun getPresenter(): MapPresenter<MapMvp.View> {
        return mPresenter
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if(slidePanel.isClosed)
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
        Log.d("uiuiui",slidePanel.isClosed.toString())
        Log.d("uiuiui",slidePanel.isOpened.toString())

        if(type == "info")
            fragmentCreatePingBasic.type = type

        if(state == "extended"){
            changeCreateGroupFragment()
        }

        fragmentCreatePingBasic.updateData(task,descr)
        //mPresenter.getAllSubGroups()
    }

    override fun updateCheckedGroups(checked: List<String>) {
        fragmentCreatePingDetails.mListAdapter.updateCheckedGroups(checked)
    }

    override fun openManageActivity() {
        startActivity(Intent(this, ManageSubGroupsActivity::class.java))
    }

    override fun openMapActivity(){
        startActivity(Intent(this, MapActivity::class.java))
        finish()
    }

    override fun showEndPingBar(ping: Ping) {
        Log.d("ioioioio",ping.inProgress.toString())

        val view = layoutInflater.inflate(R.layout.dialog_ping,null)

        var text = ""
        for(group in ping.targetGroups){
           text =  text.plus("$group,")
        }

        if(ping.progressorName.isNotEmpty()){
            view.fieldProgressor.text = ping.progressorName
            view.fieldProgressor.visibility = View.VISIBLE
        }
        else
            view.fieldProgressor.visibility = View.GONE

        view.fieldTitle.text = ping.title
        view.textSubGroups.text = text
        view.textAuthor.text = ping.creatorName
        view.textDescr.text = ping.desc

        if(ping.inProgress && !ping.ended){
            view.textStatus.text = "W trakcie robienia"
            view.textStatus.setTextColor(ContextCompat.getColor(this,R.color.orange))
            view.buttonInProgress.isEnabled = false
            view.buttonInProgress.alpha = 0.5f
        }
        else if(ping.ended) {
            view.textStatus.text = "Zakończone"
            view.textStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
            view.buttonInProgress.isEnabled = false
            view.buttonInProgress.alpha = 0.5f
            view.buttonSetToEnd.isEnabled = false
            view.buttonSetToEnd.alpha = 0.5f
        }
        else{
            view.textStatus.text = "Nie rozpoczęte"
        }

        val builder = AlertDialog.Builder(this)
                .setView(view)

        val dialog = builder.create()
        dialog.show()

        view.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        view.buttonInProgress.setOnClickListener {
            mPresenter.onInProgressClick(ping.pingId)
            dialog.dismiss()
        }
        view.buttonSetToEnd.setOnClickListener {
            mPresenter.onEndPing(ping.pingId)
            dialog.dismiss()
        }



    }

    override fun openHistoryFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragmentHistory)
                .addToBackStack(null)
                .commit()
        slidePanel.openLayer(true)
    }

    override fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true

    }

    override fun clearPings() {
        mMapHelper.clearPings()
    }

    override fun showPlanDialog() {
        val datePicker = DatePicker(this)
        val viewDate = layoutInflater.inflate(R.layout.dialog_time,null)

        var date = ""

        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        viewDate.buttonDate.text = "${month}/${day}/${year}"

        val dialogCalendar = AlertDialog.Builder(this)
                .setView(datePicker)
                .setPositiveButton("Wybierz") { dialogInterface, i ->
                    Log.d("bnmbnm","bnmbnm")
                    viewDate.buttonDate.text ="${datePicker.month+1}/${datePicker.dayOfMonth}/${datePicker.year}"
                }.create()

        viewDate.buttonDate.setOnClickListener {
            dialogCalendar.show()
        }

        val builder = AlertDialog.Builder(this)
                .setMessage("Wybierz datę")
                .setView(viewDate)
                .setPositiveButton("Zaplanuj") { dialogInterface, i ->
                    try {
                        val format = java.text.SimpleDateFormat("EEE MMM dd YYYY HH:mm:ss z",Locale.ENGLISH)
                        val date = Date()
                        val calendar = Calendar.getInstance()
                        calendar.set(datePicker.year,datePicker.month,datePicker.dayOfMonth,
                                viewDate.timePicker.currentHour,viewDate.timePicker.currentMinute,0)
                        date.time = calendar.timeInMillis

                        val time = format.format(date)
                        Log.d("time",time)
                        mPresenter.onAddButtonClick(date = time)

                    }catch (e: Exception){
                        Log.d("asdasdads","asdssad")
                    }
                }
                .setNegativeButton("Anuluj") { _, _ ->

                }

        builder.create().show()

    }

    fun showSlide(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragment)
                .addToBackStack(null)
                .commit()

        if(slidePanel.isClosed)
            slidePanel.openLayer(true)
    }

    override fun onBackPressed() {
        if(slidePanel.isOpened)
            slidePanel.closeLayer(true)

        if (fragmentCreatePingBasic.isAdded)
            supportFragmentManager.beginTransaction()
                    .remove(fragmentCreatePingBasic)
                    .commit()
        if (fragmentCreatePingDetails.isAdded)
            supportFragmentManager.beginTransaction()
                    .remove(fragmentCreatePingDetails)
                    .commit()

        if(fragmentOptions.isAdded)
            supportFragmentManager.beginTransaction()
                    .remove(fragmentOptions)
                    .commit()

        super.onBackPressed()


    }

    override fun removePing(pingId: String) {
       mMapHelper.removePing(pingId)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.item_hamburger ->{
                drawerMap.openDrawer()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun replaceFragment(fragment: Fragment, id: Int){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .commit()
    }
}
