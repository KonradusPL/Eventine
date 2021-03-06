package com.racjonalnytraktor.findme3.ui.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import androidx.core.view.GravityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.WindowManager
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.sectionItem
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.ui.adapters.PageAdapterMain
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import com.racjonalnytraktor.findme3.utils.CircleTransform

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.email
import org.jetbrains.anko.uiThread


class MainActivity : BaseActivity(),MainMvp.View {

    lateinit var mPresenter: MainPresenter<MainMvp.View>
    var mMenu: Menu? = null
    private lateinit var drawerMain: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewPager()

        mPresenter = MainPresenter()
        mPresenter.onAttach(this)

        setSupportActionBar(toolbarMain)

        val actionBar = supportActionBar

        //actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        //actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = ""
        //actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        toolbarMain.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        toolbarMain.setNavigationOnClickListener {
            drawerMain.openDrawer()
        }
    }

    override fun setUpLeftNavigation(groups: ArrayList<Group>, user: User) {
        drawerMain = drawer {

            gravity = GravityCompat.START

            accountHeader {
                profile(user.fullName,user.email)
                background = Color.WHITE
                textColor = Color.BLACK.toLong()
            }

            //headerView = LayoutInflater.from(this@MainActivity).inflate(R.layout.navigation_header,null)
            primaryItem("Wyloguj się"){
                icon = R.drawable.ic_directions_run_black_24dp
                tag = "logout"
            }

        }
        drawerMain.deselect()
       /* for(group in groups)
            drawerMain.addItem(PrimaryDrawerItem()
                    .withName(group.groupName)
                    .withTag(group.groupName))*/

        drawerMain.setOnDrawerItemClickListener{view, position, drawerItem ->
            if(drawerItem.tag is String){
                if(drawerItem.tag == "logout")
                    mPresenter.onLogoutButtonClick()
            }
            return@setOnDrawerItemClickListener true
        }

        //drawerMap.addItem(DrawerIte)
        //drawerMap.addItem(IDrawerItem<>)
        /* navigationMap.setNavigationItemSelectedListener { menuItem ->
             when (menuItem?.itemId) {
                 R.id.item_logout ->{
                     mPresenter.onLogoutButtonClick()
                     true
                 }
                 R.id.item_change ->{

                 }
             }
         }*/
    }

    private fun setUpViewPager(){
        val pageAdapter = PageAdapterMain(supportFragmentManager, this@MainActivity)
        viewPagerMain.adapter = pageAdapter

        tabLayoutMain.setupWithViewPager(viewPagerMain)

        for (i in 0..tabLayoutMain.tabCount-1) {
            val tab = tabLayoutMain.getTabAt(i)
            tab!!.customView = pageAdapter.getTabView(i)
        }

        tabLayoutMain.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                pageAdapter.setTabColor(false,tab!!.customView!!,applicationContext)
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pageAdapter.setTabColor(true,tab!!.customView!!,applicationContext)
            }
        })
        viewPagerMain.currentItem = 0

    }

    override fun changeProfileIcon(url: String) {

    }

    override fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun openMainActivity(){
        startActivity(Intent(this,MapActivity::class.java))
    }

}
