package com.racjonalnytraktor.findme3.ui.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon

import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import com.racjonalnytraktor.findme3.ui.main.fragments.join.JoinFragment
import com.racjonalnytraktor.findme3.ui.main.fragments.create.CreateGroupFragment
import com.racjonalnytraktor.findme3.ui.main.fragments.feed.FeedFragment
import kotlinx.android.synthetic.main.custom_tab.view.*


class PageAdapterMain(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {

    private val tabIcons = ArrayList<IIcon>()

    val tabNames = context.resources.getStringArray(R.array.tab_names_main)

    init {
        tabIcons.add(GoogleMaterial.Icon.gmd_people_outline)
        tabIcons.add(GoogleMaterial.Icon.gmd_home)
        tabIcons.add(GoogleMaterial.Icon.gmd_add)
    }


    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return CreateGroupFragment<MainMvp.View>()
            1 -> return FeedFragment<MainMvp.View>()
            2 -> return JoinFragment<MainMvp.View>()
        }
        return Fragment()
    }

    override fun getCount(): Int {
        return 3
    }

    fun getTabView(position: Int) : View{
        val view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        view.tabTitle.text = tabNames[position]
        view.tabIcon.icon = IconicsDrawable(context)
                .icon(tabIcons[position])
                .color(Color.WHITE)
                .sizeDp(18)


        when(position){
            0 -> setTabColor(true,view,context)
            else -> setTabColor(false,view,context)
        }

        return view
    }

    fun setTabColor(selected: Boolean, view: View, ctx: Context){
        when(selected){
            true -> {
                view.tabTitle.textSize = 14f
                view.tabIcon.icon.sizeDp(20)
                view.tabTitle.typeface = Typeface.DEFAULT_BOLD
                // view.tabIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.colorPrimaryDark))
                //view.tabTitle.setTextColor(ctx.resources.getColor(R.color.colorPrimaryDark))
            }
            false ->{
                view.tabTitle.typeface = Typeface.DEFAULT
                view.tabIcon.icon.sizeDp(18)
                view.tabTitle.textSize = 12f
                //view.tabIcon.setColorFilter(ContextCompat.getColor(ctx,R.color.dimGrey))
                //view.tabTitle.setTextColor(ctx.resources.getColor(R.color.dimGrey))
            }

        }

    }

}