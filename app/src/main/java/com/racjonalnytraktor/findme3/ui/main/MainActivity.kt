package com.racjonalnytraktor.findme3.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.racjonalnytraktor.findme3.ui.adapters.PageAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(),MainMvp.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pageAdapter = PageAdapter(supportFragmentManager, this@MainActivity)
        viewPagerMain.adapter = pageAdapter

        tabLayoutMain.setupWithViewPager(viewPagerMain)
        viewPagerMain.currentItem = 1

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
}
