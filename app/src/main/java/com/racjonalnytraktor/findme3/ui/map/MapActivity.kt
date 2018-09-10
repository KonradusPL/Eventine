package com.racjonalnytraktor.findme3.ui.map

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.DatePicker
import android.widget.TextView
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
import com.wunderlist.slidinglayer.SlidingLayer
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.dialog_ping.view.*
import kotlinx.android.synthetic.main.dialog_time.view.*
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.item_tab.view.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.math.abs

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
    lateinit var fragmentAddTask: AddTaskFragment<MapMvp.View>

    var circleClickedPosition = floatArrayOf(0f,0f)

    lateinit var drawerMap: Drawer

    private var tabStatus = 1


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
        fragmentAddTask = AddTaskFragment()

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

        val organiserIcon = IconicsDrawable(this)
                .color(Color.WHITE)
                .sizeDp(22)
                .icon(FontAwesome.Icon.faw_user_alt)

        val helpIcon = IconicsDrawable(this)
                .color(Color.WHITE)
                .sizeDp(22)
                .icon(FontAwesome.Icon.faw_question_circle)

        textOrganiser.setCompoundDrawables(null,organiserIcon,null,null)
        textHelp.setCompoundDrawables(null,helpIcon,null,null)

        initTabs()
        setUpClickListeners()

        mPresenter.onAttach(this)

        listenSlidingState()
        fragmentMap.getMapAsync(mMapHelper)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
    }

    private fun setUpClickListeners(){
        bottomCircle.setOnClickListener {
            //animateTabLayout()
            mPresenter.onCircleClick(biggerCircleContainer.visibility)
            //showSlide(fragmentAddTask)
        }
        biggerCircle.setOnTouchListener { view, event ->
            if(event.action != MotionEvent.ACTION_DOWN)
                return@setOnTouchListener false
            val size = Point()
            windowManager.defaultDisplay.getSize(size)

            if (event.rawX + biggerCircle.x < size.x/2)
                mPresenter.onOrganiserClick()
            else
                mPresenter.onHelpClick()

            return@setOnTouchListener false
        }
        biggerCircleContainer.setOnClickListener{
            //view -> view.place
        }

    }

    private fun listenSlidingState() {
        var isSliderClosed: Boolean = slidePanel.isClosed
        Log.d("isClosed", isSliderClosed.toString())
        doAsync {
            while (!isDestroyed) {
                Thread.sleep(100)
                if(!isSliderClosed && isSliderClosed != slidePanel.isClosed){
                    uiThread {
                        if(tabStatus == 0)
                            animateTabLayout()
                        if(fragmentCreatePingBasic.isAdded)
                            fragmentCreatePingBasic.clearData()
                        mPresenter.clearData()
                        if(fragmentCreatePingDetails.isInLayout){
                            fragmentCreatePingDetails.clearData()

                        }

                        tabLayoutMap.isSelected = false

                    }

                }
                isSliderClosed = slidePanel.isClosed
            }
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
                    1 -> mPresenter.onHistoryButtonClick()
                    0 -> replaceFragment(fragmentOptions,R.id.fragmentContainer)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val color = ContextCompat.getColor(this@MapActivity,R.color.greyTab)
                tab?.customView?.text?.setTextColor(color)
                tab?.customView?.icon?.icon?.color(color)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("method","onTabSelected")
                val fragment: Fragment
                when(tab!!.position){
                    2 -> openManageActivity()
                    1 -> mPresenter.onHistoryButtonClick()
                    0 -> replaceFragment(fragmentOptions,R.id.fragmentContainer)
                }
                val color = ContextCompat.getColor(this@MapActivity,R.color.colorPrimaryNew)
                tab.customView?.text?.setTextColor(color)
                tab.customView?.icon?.icon?.color(color)

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

        view.apply {
            fieldTitle.text = ping.title
            textSubGroups.text = text
            textAuthor.text = ping.creatorName
            textDescr.text = ping.desc
        }

        if(ping.inProgress && !ping.ended){
            view.apply {
                textStatus.text = "W trakcie robienia"
                textStatus.setTextColor(ContextCompat.getColor(this@MapActivity,R.color.orange))
                buttonInProgress.isEnabled = false
                buttonInProgress.alpha = 0.5f
            }

        }
        else if(ping.ended) {
            view.apply {
                textStatus.text = "Zakończone"
                textStatus.setTextColor(ContextCompat.getColor(this@MapActivity, R.color.green))
                buttonInProgress.isEnabled = false
                buttonInProgress.alpha = 0.5f
                buttonSetToEnd.isEnabled = false
                buttonSetToEnd.alpha = 0.5f
            }

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

    override fun showSlide(type: String){
        val fragment = when(type){
            "history" -> fragmentHistory
            "addTask" -> fragmentAddTask
            else -> Fragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragment)
                .commit()

        if(slidePanel.isClosed)
            slidePanel.openLayer(true)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.beginTransaction().isEmpty)
            super.onBackPressed()

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
    }

    override fun animateExtendedCircle(show: Boolean) {
        if(!show && biggerCircleContainer.visibility == View.GONE)
            return
        if(show)
            biggerCircleContainer.visibility = View.VISIBLE


        /*val animation = if(show) AlphaAnimation(0f,1f) else AlphaAnimation(1f,0f)

        animation.duration = 300
        //animation.startOffset = 50
        animation.fillAfter = true
        biggerCircleContainer.startAnimation(animation)*/

        if(!show)
            biggerCircleContainer.visibility = View.GONE
    }

    override fun animateTabLayout(){
        Log.d("plokpl","plokpl")
        val constraint1 = ConstraintSet()
        constraint1.clone(this, R.layout.activity_map)
        val constraint2 = ConstraintSet()
        constraint2.clone(this, R.layout.activity_map_hide_tab)
        TransitionManager.beginDelayedTransition(root)
        val constraint = if(tabStatus == 0) constraint1 else constraint2
        constraint.applyTo(root)
        tabStatus = if(tabStatus == 1) 0 else 1
        Log.d("tabStatus",tabStatus.toString())
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
