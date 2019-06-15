package com.racjonalnytraktor.findme3.ui.map.fragments

import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.ManageGroupAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.listeners.Listener
import kotlinx.android.synthetic.main.fragment_organiser.*

class OrganisersFragment<V: MapMvp.View>: BaseFragment<V>(),Listener.Organisers {

    private var mListAdapter: ManageGroupAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_organiser,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iconArrow.setImageDrawable(IconicsDrawable(parentContext)
                .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                .sizeDp(24)
                .color(Color.BLACK))
        iconArrow.setOnClickListener {
            parentMvp.getPresenter().onBackInFragmentClick("organizer")
        }
    }

    override fun showList(list: ArrayList<Job>) {
        val arrayList = ArrayList<Job>()
        arrayList.addAll(list)
        mListAdapter = ManageGroupAdapter(arrayList,parentMvp,"organizer")

        listOrganisers.layoutManager = LinearLayoutManager(parentMvp.getCtx())
        listOrganisers.adapter = mListAdapter
    }

    override fun onStart() {
        super.onStart()
        parentMvp.getPresenter().onOrganisersAttach(this)
    }

    override fun showLoading() {
        progressOrganisers?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressOrganisers?.visibility = View.GONE
    }
}