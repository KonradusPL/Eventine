package com.racjonalnytraktor.findme3.ui.main

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import android.support.design.widget.TabLayout
import android.view.Menu
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.racjonalnytraktor.findme3.ui.adapters.PageAdapterMain
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import com.racjonalnytraktor.findme3.utils.CircleTransform
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : BaseActivity(),MainMvp.View {

    lateinit var mPresenter: MainPresenter<MainMvp.View>
    var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewPager()

        setUpLeftNavigation()

        mPresenter = MainPresenter()
        mPresenter.onAttach(this)
    }

    private fun setUpLeftNavigation() {
        navigationMain.setNavigationItemSelectedListener { menuItem ->
            mPresenter.onLogoutButtonClick()
            true
        }
    }

    private fun setUpViewPager(){
        val pageAdapter = PageAdapterMain(supportFragmentManager, this@MainActivity)
        viewPagerMain.adapter = pageAdapter

        tabLayoutMain.setupWithViewPager(viewPagerMain)
        viewPagerMain.currentItem = 0

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
    }

    override fun changeProfileIcon(url: String) {

    }

    override fun openLoginActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

}
