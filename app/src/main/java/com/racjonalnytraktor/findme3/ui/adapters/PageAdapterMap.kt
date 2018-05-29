package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import com.racjonalnytraktor.findme3.ui.main.fragments.create.CreateGroupFragment
import com.racjonalnytraktor.findme3.ui.main.fragments.feed.FeedFragment
import com.racjonalnytraktor.findme3.ui.main.fragments.join.JoinFragment
import kotlinx.android.synthetic.main.custom_tab.view.*


class PageAdapterMap(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    private val tabIcons = intArrayOf(
            R.drawable.ic_settings_black_24dp,
            R.drawable.ic_history_black_24dp,
            R.drawable.ic_pin_drop_black_24dp,
            R.drawable.ic_info_black_24dp,
            R.drawable.ic_group_black_24dp)

    val tabNames = context.resources.getStringArray(R.array.tab_names_map)


    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return FeedFragment<MainMvp.View>()
            1 -> return CreateGroupFragment<MainMvp.View>()
            2 -> return JoinFragment<MainMvp.View>()
        //3 -> return ProfileFragment()
        }
        return Fragment()
    }

    override fun getCount(): Int {
        return 5
    }

    fun getTabView(position: Int) : View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        view.tabTitle.text = tabNames[position]
        view.tabIcon.setImageResource(tabIcons[position])

        when(position){
            0 -> setTabColor(true,view,context)
            else -> setTabColor(false,view,context)
        }

        return view
    }

    fun setTabColor(selected: Boolean, view: View, ctx: Context){
        when(selected){
            true -> {
                view.tabIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.colorPrimaryDark))
                view.tabTitle.setTextColor(ctx.resources.getColor(R.color.colorPrimaryDark))
            }
            false ->{
                view.tabIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.black))
                view.tabTitle.setTextColor(ctx.resources.getColor(R.color.black))
            }

        }

    }
}