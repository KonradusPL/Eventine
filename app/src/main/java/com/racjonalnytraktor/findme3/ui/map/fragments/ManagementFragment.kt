package com.racjonalnytraktor.findme3.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp


class ManagementFragment: BaseFragment<MapMvp.View>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_management,container,false)
    }
}