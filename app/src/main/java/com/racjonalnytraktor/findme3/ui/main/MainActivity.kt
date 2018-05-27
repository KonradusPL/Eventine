package com.racjonalnytraktor.findme3.ui.main

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import androidx.view.get
import com.racjonalnytraktor.findme3.ui.adapters.PageAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap
import android.util.Log
import com.racjonalnytraktor.findme3.utils.CircleTransform
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MainActivity : BaseActivity(),MainMvp.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar()

        setUpViewPager()
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbarMain)
    }

    private fun setUpViewPager(){
        val pageAdapter = PageAdapter(supportFragmentManager, this@MainActivity)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_toolbar,menu)

        doAsync {
            val bitmap = Picasso.get()
                    .load("https://i2.wp.com/startupkids.pl/wp-content/uploads/2018/01/kuba-mularski-250x343-supervisor.jpg?fit=250%2C343")
                    .transform(CircleTransform())
                    .get()

            val drawable = BitmapDrawable(resources,bitmap)
            uiThread {
                menu!!.getItem(0).icon = drawable
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
