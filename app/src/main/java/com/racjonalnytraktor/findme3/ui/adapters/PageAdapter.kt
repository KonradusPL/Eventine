package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import com.racjonalnytraktor.findme3.R
import android.widget.TextView
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import com.racjonalnytraktor.findme3.ui.main.fragments.JoinGroupFragment
import com.racjonalnytraktor.findme3.ui.main.fragments.ProfileFragment
import com.racjonalnytraktor.findme3.ui.main.fragments.create.CreateGroupFragment
import kotlinx.android.synthetic.main.custom_tab.view.*


class PageAdapter(fm: FragmentManager,val context: Context): FragmentPagerAdapter(fm) {

    private val tabIcons = intArrayOf(
            R.drawable.ic_add_black_24dp,
            R.drawable.ic_group_add_black_24dp,
            R.drawable.ic_person_black_24dp)

    val tabNames = context.resources.getStringArray(R.array.tab_names)


    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return CreateGroupFragment<MainMvp.View>()
            1 -> return JoinGroupFragment()
            2 -> return ProfileFragment()
        }
        return Fragment()
    }

    override fun getCount(): Int {
        return 3
    }

    fun getTabView(position: Int) : View{
        val view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        view.tabTitle.text = tabNames[position]
        view.tabIcon.setImageResource(tabIcons[position])

        when(position){
            1 -> setTabColor(true,view,context)
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
                view.tabIcon.setColorFilter(ContextCompat.getColor(ctx,R.color.black))
                view.tabTitle.setTextColor(ctx.resources.getColor(R.color.black))
            }

        }

    }

}