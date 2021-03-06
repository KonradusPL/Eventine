package com.racjonalnytraktor.findme3.ui.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.AdapterView
import com.estimote.proximity_sdk.api.ProximityObserver
import com.google.android.gms.maps.SupportMapFragment
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.Drawer
import com.racjonalnytraktor.findme3.ui.manage.ManageSubGroupsActivity
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.map.ZoneUpdate
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.AppClass
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.profile.ProfileFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.*
import com.racjonalnytraktor.findme3.ui.map.fragments.addtask.AddTaskFragment
import com.racjonalnytraktor.findme3.utils.MapHelper
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.item_tab.view.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//https://xd.adobe.com/spec/39ba5ff1-b994-4bea-507d-1cf1b10031a9-4f8b/

class MapActivity : BaseActivity(),MapMvp.View{

    lateinit var mMapHelper: MapHelper
    lateinit var mPresenter: MapPresenter<MapMvp.View>

    private var mObservationHandler: ProximityObserver.Handler? = null

    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var fragmentManagement: ManagementFragment
    private lateinit var fragmentHistory: HistoryFragment<MapMvp.View>
    private lateinit var fragmentOptions: SettingsFragment
    private lateinit var fragmentProfile: ProfileFragment
    private lateinit var fragmentAddTask: AddTaskFragment<MapMvp.View>
    private lateinit var fragmentManageGroup: ManageGroupFragment<MapMvp.View>
    private lateinit var fragmentOrganisers: OrganisersFragment<MapMvp.View>

    var circleClickedPosition = floatArrayOf(0f,0f)

    lateinit var drawerMap: Drawer

    private var mCurrentSlide = ""
    private var circleStatus = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mPresenter = MapPresenter()
        mMapHelper = MapHelper(this)

        fragmentMap = SupportMapFragment.newInstance()
        fragmentManagement = ManagementFragment()
        fragmentHistory = HistoryFragment()
        fragmentOptions = SettingsFragment()
        fragmentAddTask = AddTaskFragment()
        fragmentManageGroup = ManageGroupFragment()
        fragmentProfile = ProfileFragment()
        fragmentOrganisers = OrganisersFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer,fragmentMap)
                .commit()

        fragmentMap.getMapAsync(mMapHelper)

        initTabs()
        initOtherViews()
        initFloorSpinner()
        setUpClickListeners()

        listenSlidingState()

        initBeaconsScanning()

        mPresenter.onAttach(this)
    }

    private fun initOtherViews(){
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
    }

    private fun setUpClickListeners(){
        bottomCircle.setOnClickListener {
            mPresenter.onCircleClick(biggerCircleContainer.visibility)
        }
        biggerCircle.setOnTouchListener { view, event ->
            if(event.action != MotionEvent.ACTION_DOWN)
                return@setOnTouchListener false
            val size = Point()
            windowManager.defaultDisplay.getSize(size)

            if (event.rawX + biggerCircle.x < size.x/2)
                mPresenter.onOrganiserButtonClick()
            else
                mPresenter.onHelpClick()

            return@setOnTouchListener false
        }
        biggerCircleContainer.setOnClickListener{
            //view -> view.place
        }
    }

    private fun initTabs(){
        val icons = arrayListOf<IIcon>(
                FontAwesome.Icon.faw_cog,
                FontAwesome.Icon.faw_history,
                FontAwesome.Icon.faw_users,
                FontAwesome.Icon.faw_users,
                FontAwesome.Icon.faw_user_alt)
        val titles = arrayListOf("Opcje","Historia","nic","Grupy","Profil")

        for(i in 0..4){
            val tabView = LayoutInflater.from(this).inflate(R.layout.item_tab,null,false)
            if(i != 2){
                tabView.icon.icon = IconicsDrawable(this)
                        .icon(icons[i])
                        .color(ContextCompat.getColor(this,R.color.greyTab))
                        .sizeDp(22)
                tabView.text.text = titles[i]
            }
            if(i == 2 || i == 4){
                tabView.isClickable = false
            }
            tabLayoutMap.addTab(tabLayoutMap.newTab().setCustomView(tabView))
        }
        tabLayoutMap.addOnTabSelectedListener(object:  TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val fragment: Fragment

                when(tab!!.position){
                    1 -> mPresenter.onHistoryButtonClick()
                    0 -> mPresenter.onOptionsClick()
                    3 -> mPresenter.onGroupsClick()
                    4 -> mPresenter.onProfileClick()
                }
                val color = ContextCompat.getColor(this@MapActivity,R.color.colorPrimaryNew)
                tab.customView?.text?.setTextColor(color)
                tab.customView?.icon?.icon?.color(color)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                clearTab(tab?.position ?: 1)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("method","onTabSelected")
                val fragment: Fragment
                when(tab!!.position){
                    1 -> mPresenter.onHistoryButtonClick()
                    0 -> mPresenter.onOptionsClick()
                    3 -> mPresenter.onGroupsClick()
                    4 -> mPresenter.onProfileClick()
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

        tabLayoutMap.getTabAt(2)?.select()
    }

    private fun initFloorSpinner(){
        val floors = arrayListOf("-1","0","1")
        spinnerFloor.attachDataSource(floors)
        spinnerFloor.selectedIndex = 1
        spinnerFloor.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("initFloorSpinner",p2.toString())
                mPresenter.onFloorSelected(p2)
                mMapHelper.onChangeFloor(floors[p2].toInt())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        })
    }

    private fun initBeaconsScanning(){
        (application as AppClass).initBeaconsScanning(this)
    }

    private fun listenSlidingState() {
        var isSliderClosed: Boolean = slidePanel.isClosed
        Log.d("isClosed", isSliderClosed.toString())
        doAsync {
            while (!isDestroyed) {
                Thread.sleep(100)
                if(!isSliderClosed && isSliderClosed != slidePanel.isClosed){
                    uiThread {
                        val n = tabLayoutMap.selectedTabPosition
                        if(n > -1 && n < 5 && n != 2){
                            if(mCurrentSlide == "history")
                                clearTab(1)
                        }

                        mPresenter.onSlideHide()

                        tabLayoutMap.isSelected = false

                    }

                }
                isSliderClosed = slidePanel.isClosed
            }
        }
    }

    override fun clearTab(position: Int, auto: Boolean){
        var pos = position
        if(auto)
            pos = tabLayoutMap.selectedTabPosition
        val color = ContextCompat.getColor(this@MapActivity,R.color.greyTab)
        tabLayoutMap.getTabAt(pos)?.customView?.text?.setTextColor(color)
        tabLayoutMap.getTabAt(pos)?.customView?.icon?.icon?.color(color)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdate(location: LocationEvent) {
    }

    override fun onStart() {
        super.onStart()
        Log.d("lifecycler","onStart")
        mPresenter.isAttached = true
        EventBus.getDefault().register(this)
        mPresenter.startUpdatingMap()
    }

    override fun onStop() {
        super.onStop()
        Log.d("lifecycler","onStop")
        EventBus.getDefault().unregister(this)
        mPresenter.isAttached = false
        mObservationHandler?.stop()
    }

    override fun updateMapImage(bitmap: Bitmap) {
        if(isFullFragmentAdded())
            imageMap.visibility = View.VISIBLE
        //imageMap.setImageBitmap(bitmap)
        Blurry.with(this)
                .from(bitmap)
                .into(imageMap)
    }

   private fun showBlur() {
        mMapHelper.getImage()
    }

    private fun hideBlur() {
        imageMap.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDetach()
    }

    override fun updateSubGroups(item: String) {
    }

    override fun addPing(ping: Ping) {
        mMapHelper.addPing(ping,true)
    }

    override fun updatePings(pings: List<Ping>,floor: Int) {
        mMapHelper.updatePings(pings,floor)
    }

    override fun updateZones(zones: ArrayList<ZoneUpdate>) {
        mMapHelper.updateZones(zones)
    }

    override fun updatePings(ping: Action) {
        //mMapHelper.updatePings(actions)
    }

    override fun getPresenter(): MapPresenter<MapMvp.View> {
        return mPresenter
    }

    override fun updateCheckedGroups(checked: List<String>) {

    }

    override fun openManageActivity() {
        startActivity(Intent(this, ManageSubGroupsActivity::class.java))
    }

    override fun openMapActivity(){
        startActivity(Intent(this, MapActivity::class.java))
        finish()
    }

    override fun showEndPingBar(ping: Ping) {
        val builder = AlertDialog.Builder(this)
                .setTitle(ping.title)
                .setMessage(ping.desc)
        if(ping.inProgress){
            builder.setPositiveButton("Zakończ") { _, i ->
                mPresenter.onEndPingClick(ping.pingId)
            }
        }
        else if(!ping.ended){
            builder.setPositiveButton("Zakończ") { _, i ->
                mPresenter.onEndPingClick(ping.pingId)
            }
            builder.setNegativeButton("Zacznij"){ _, i ->
                mPresenter.onInProgressClick(ping.pingId)
            }
        }
        builder.setNeutralButton("Anuluj") { _, i -> }

        builder.create().show()
}



    override fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun clearPings() {
        mMapHelper.clearPings()
    }

    override fun showPlanDialog() {}

    override fun showSlide(type: String, location: Location){
        val fragment = when(type){
            "history" -> fragmentHistory
            "addTask" -> fragmentAddTask
            "organizer" -> fragmentOrganisers
            else -> Fragment()
        }
        mCurrentSlide = type

        if(location.latitude != 0.0 && type == "addTask"){
            fragmentAddTask.changeLocation(location)
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.containerSlide,fragment)
                .commit()

        if(slidePanel.isClosed)
            slidePanel.openLayer(true)
    }

    override fun hideSlide() {
        if(slidePanel.isOpened)
            slidePanel.closeLayer(true)
    }

    override fun removeFragment(type: String){
        val fragment = when(type){
            "history" -> fragmentHistory
            "addTask" -> fragmentAddTask
            "organizer" -> fragmentOrganisers
            else -> Fragment()
        }
        supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
    }

    override fun showFullFragments(type: String) {
        val fragment = when(type){
            "options" -> fragmentOptions
            "groups" -> fragmentManageGroup
            "profile" -> fragmentProfile
            else -> Fragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .commit()

        spinnerFloor.visibility = View.GONE
        showBlur()

        if(slidePanel.isOpened)
            slidePanel.closeLayer(true)
    }

    override fun hideFullFragments(type: String, unSelectTab: Boolean) {
        val fragment = when(type){
            "options" -> fragmentOptions
            "groups" -> fragmentManageGroup
            "profile" -> fragmentProfile
            else -> Fragment()
        }
        supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()

        spinnerFloor.visibility = View.VISIBLE
        hideBlur()

        if(unSelectTab)
            tabLayoutMap.getTabAt(2)?.select()
    }

    override fun hideFullFragments() {
        if(!isFullFragmentAdded())
            return

        var fragment: Fragment? = null

        spinnerFloor.visibility = View.VISIBLE
        hideBlur()

        when {
            fragmentOptions.isAdded -> fragment = fragmentOptions
            fragmentManageGroup.isAdded -> fragment = fragmentManageGroup
            fragmentProfile.isAdded -> fragment = fragmentProfile
        }

        if(fragment != null)
            supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()

    }

    override fun updateOnPartner() {
        textHelp.text = "Opiekun"
    }

    override fun changeBeaconsStatus(enable: Boolean) {
        (application as AppClass).changeBeaconsStatus(enable,this)
    }

    override fun showLoading() {
        progressMap?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressMap?.visibility = View.GONE
    }

    override fun onBackPressed() {
        var showExit = true
        if(slidePanel.isOpened){
            slidePanel.closeLayer(true)
            showExit = false
        }

        if(fragmentOptions.isAdded)
            mPresenter.onBackInFragmentClick("options")

        else if(fragmentManageGroup.isAdded)
            mPresenter.onBackInFragmentClick("groups")

        else if(fragmentProfile.isAdded)
            mPresenter.onBackInFragmentClick("profile")

        else if(showExit){
            AlertDialog.Builder(this)
                    .setTitle("Wyjście z aplikacji")
                    .setMessage("Czy napewno chcesz wyjść z aplikacji?")
                    .setPositiveButton("Tak") { dialogInterface, i ->
                        finish()
                    }
                    .setNegativeButton("Nie") { dialogInterface, i ->
                    }.create().show()
        }
    }

    override fun animateExtendedCircle(show: Boolean) {
        if(!show && biggerCircleContainer.visibility == View.GONE)
            return
        if(show){
            //circleStatus = 2
            biggerCircleContainer.visibility = View.VISIBLE
            textAddTask.visibility = View.VISIBLE
            iconCircle.visibility = View.GONE
        }

        if(!show){
            circleStatus = 1
            biggerCircleContainer.visibility = View.GONE
            textAddTask.visibility = View.GONE
            iconCircle.visibility = View.VISIBLE

        }
    }

    override fun animateTabLayout(show: Boolean){
        val constraint1 = ConstraintSet()
        constraint1.clone(this, R.layout.activity_map)
        val constraint2 = ConstraintSet()
        constraint2.clone(this, R.layout.activity_map_hide_tab)

        TransitionManager.beginDelayedTransition(root)

        val constraint = if(show) constraint1 else constraint2
        constraint.applyTo(root)
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

    private fun isFullFragmentAdded() = fragmentProfile.isAdded || fragmentManageGroup.isAdded
            || fragmentOptions.isAdded
}
