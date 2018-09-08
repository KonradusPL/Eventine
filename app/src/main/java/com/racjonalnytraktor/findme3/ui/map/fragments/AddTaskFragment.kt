package com.racjonalnytraktor.findme3.ui.map.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.fragment_add_task.*

class AddTaskFragment <V: MapMvp.View>: BaseFragment<V>() {

    var firstFragment = DescriptionFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_add_task,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
                .replace(R.id.detailsContainer,firstFragment)
                .commit()

        iconArrowTask.icon = IconicsDrawable(parentContext)
                .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                .sizeDp(24)
                .color(Color.BLACK)
    }

}