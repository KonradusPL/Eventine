package com.racjonalnytraktor.findme3.ui.main.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainMvp

class GroupsFragment<V: MainMvp.View>: BaseFragment<V>(), GroupsMvp.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups,container,false)
    }
}