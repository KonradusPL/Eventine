package com.racjonalnytraktor.findme3.ui.map.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.adapters.InvitationsAdapter
import com.racjonalnytraktor.findme3.ui.adapters.SubGroupsAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.MapPresenter
import kotlinx.android.synthetic.main.fragment_create_group_basic.*
import kotlinx.android.synthetic.main.fragment_create_group_extended.*

class CreatePingDetailsFragment<V: MapMvp.View>: BaseFragment<V>() {

    var type = "ping"

    lateinit var mPresenter: MapPresenter<MapMvp.View>
    lateinit var mListAdapter: SubGroupsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_group_extended,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        buttonAdd.setOnClickListener {
            mPresenter.onAddButtonClick(mListAdapter.getCheckedGroups())
        }

    }



    private fun initList(){
        listSubGroups.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listSubGroups.layoutManager = layoutManager

        mListAdapter = SubGroupsAdapter(ArrayList(),parentMvp)
        listSubGroups.adapter = mListAdapter
    }

    fun updateList(item: String){
        mListAdapter.updatelist(item)
    }

    fun setCheckedList(list: List<String>){
        mListAdapter.updateCheckedGroups(list)
    }

    fun clearData(){
        mListAdapter.clear()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }
}