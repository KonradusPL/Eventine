package com.racjonalnytraktor.findme3.ui.map.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.adapters.HistoryAdapter
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.ManageGroupAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.listeners.Listener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_group_manager.*
import kotlinx.android.synthetic.main.fragment_group_manager.view.*

class ManageGroupFragment<V: MapMvp.View>: BaseFragment<V>(), Listener.Manage {

    private lateinit var mListAdapter: ManageGroupAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_group_manager,container,false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iconBack.setImageDrawable(IconicsDrawable(parentMvp.getCtx())
                .sizeDp(18)
                .color(Color.WHITE)
                .icon(GoogleMaterial.Icon.gmd_arrow_back))

        iconBack.setOnClickListener {
            parentMvp.getPresenter().onBackInFragmentClick("groups")
        }

        parentMvp.getPresenter().onManageGroupAttach(this as ManageGroupFragment<*>)

        buttonRefreshManage.setOnClickListener {
            parentMvp.getPresenter().onManageGroupAttach(this as ManageGroupFragment<*>)
        }
    }

    override fun showList(list: List<Job>) {
        val arrayList = ArrayList<Job>()
        arrayList.addAll(list)
        mListAdapter = ManageGroupAdapter(arrayList,parentMvp)

        listGroups?.layoutManager = LinearLayoutManager(parentMvp.getCtx())
        listGroups?.adapter = mListAdapter
    }

    override fun showLoading() {
        buttonRefreshManage?.visibility = View.GONE
        progressManage?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressManage?.visibility = View.GONE
    }

    override fun onError() {
        buttonRefreshManage?.visibility = View.VISIBLE
    }
}