package com.racjonalnytraktor.findme3.ui.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.DatePicker
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.google.android.gms.maps.SupportMapFragment
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.Drawer
import com.racjonalnytraktor.findme3.ui.manage.ManageSubGroupsActivity
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.event_bus.LocationEvent
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.AppClass
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.main.fragments.ProfileFragment
import com.racjonalnytraktor.findme3.ui.map.fragments.*
import com.racjonalnytraktor.findme3.ui.map.fragments.addtask.AddTaskFragment
import com.racjonalnytraktor.findme3.utils.MapHelper
import es.dmoral.toasty.Toasty
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.dialog_time.view.*
import kotlinx.android.synthetic.main.item_tab.view.*
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

//https://xd.adobe.com/spec/39ba5ff1-b994-4bea-507d-1cf1b10031a9-4f8b/

class MapActivity : BaseActivity(),MapMvp.View{

    lateinit var mMapHelper: MapHelper
    lateinit var mPresenter: MapPresenter<MapMvp.View>

    private val cloudCredentials = EstimoteCloudCredentials("indoorlocation-m4a","846401acdfecd6753a2d69750172aa67")
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

    private var tabStatus = 1
    private var circleStatus = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mPresenter = MapPresenter()
        mMapHelper = MapHelper(this,null)

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
    }

    private fun initFloorSpinner(){
        val floors = arrayListOf("-1","0","1","2","3","4")
        spinnerFloor.attachDataSource(floors)
    }

    private fun initBeaconsScanning(){
        (application as AppClass).initBeaconsScanning(this)
        /*val proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                .withBalancedPowerMode()
                .onError {Log.d("Beacons","proximityObserver error") }
                .build()
        val pokoikZone = ProximityZoneBuilder()
                .forTag("Pokoik")
                .inCustomRange(2.0)
                .onEnter {
                    Toasty.info(this@MapActivity,"Enter!").show()
                    Log.d("Beacons","Enter")
                }
                .onExit {
                    Toasty.info(this@MapActivity,"Exit!").show()
                    Log.d("Beacons","Exit")
                }
                .onContextChange {/* do something here */}
                .build()
        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                this,
                onRequirementsFulfilled = {
                    Log.d("Beacons","onRequirementsFulfilled")
                    mObservationHandler = proximityObserver.startObserving(pokoikZone)
                },
                onRequirementsMissing = {},
                onError = {}
        )*/

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
                            Log.d("tabLayoutMap","qweqweqwe")
                            tabLayoutMap.getTabAt(2)?.select()
                        }

                        mPresenter.onSlideHide()

                        tabLayoutMap.isSelected = false

                    }

                }
                isSliderClosed = slidePanel.isClosed
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationUpdate(location: LocationEvent) {
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

   fun showBlur() {

        mMapHelper.getImage()
    }

    fun hideBlur() {
        imageMap.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDetach()
    }

    override fun updateSubGroups(item: String) {
    }

    override fun showCreatePingView(type: String) {

    }

    override fun hideCreatePingView() {

    }

    override fun addPing(ping: Ping) {
        mMapHelper.addPing(ping,true)
    }

    override fun updatePings(pings: List<Ping>,value: Boolean) {
        mMapHelper.updatePings(pings)
    }

    override fun updatePings(ping: Action) {
        //mMapHelper.updatePings(actions)
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

    }

    override fun updateWithSavedData(task: String, descr: String, checked: List<String>, type: String, state: String) {

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
        if(ping.ended)
            builder.setNegativeButton("Anuluj") {dialogInterface, i ->
            }
        else if(ping.inProgress){
            builder.setPositiveButton("Zakończ") {dialogInterface, i ->
                mPresenter.onEndPingClick(ping.pingId)
            }
            builder.setNegativeButton("Anuluj") {dialogInterface, i -> }
        }
        else{
            builder.setPositiveButton("Zakończ") {dialogInterface, i ->
                mPresenter.onEndPingClick(ping.pingId)
            }
            builder.setNeutralButton("Zacznij"){dialogInterface, i ->
                mPresenter.onInProgressClick(ping.pingId)
            }
        }

        builder.create().show()


        /*Log.d("ioioioio",ping.inProgress.toString())

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
            mPresenter.onEndPingClick(ping.pingId)
            dialog.dismiss()
        }*/
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

    override fun showSlide(type: String, location: Location){
        val fragment = when(type){
            "history" -> fragmentHistory
            "addTask" -> fragmentAddTask
            "organizer" -> fragmentOrganisers
            else -> Fragment()
        }
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

    override fun changeBeaonsStatus(enable: Boolean) {
        (application as AppClass).changeBeaconsStatus(enable,this)
    }

    override fun showLoading() {
        progressMap?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressMap?.visibility = View.GONE
    }

    override fun onBackPressed() {

        if(slidePanel.isOpened)
            slidePanel.closeLayer(true)

        if(fragmentOptions.isAdded)
            mPresenter.onBackInFragmentClick("options")

        if(fragmentManageGroup.isAdded)
            mPresenter.onBackInFragmentClick("groups")

        if(fragmentProfile.isAdded)
            mPresenter.onBackInFragmentClick("profile")


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

    fun isFullFragmentAdded() = fragmentProfile.isAdded || fragmentManageGroup.isAdded
            || fragmentOptions.isAdded
}
