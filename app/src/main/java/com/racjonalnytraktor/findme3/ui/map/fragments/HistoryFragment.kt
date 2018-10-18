package com.racjonalnytraktor.findme3.ui.map.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.adapters.HistoryAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment<V: MapMvp.View>: BaseFragment<V>(),HistoryMvp.View {

    var mListAdapter: HistoryAdapter? = null
    lateinit var mPresenter: HistoryPresenter<HistoryMvp.View>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_history,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = HistoryPresenter()

        initList()

        iconArrow.setImageDrawable(IconicsDrawable(parentContext)
                .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                .sizeDp(24)
                .color(Color.BLACK))
        iconArrow.setOnClickListener {
            parentMvp.getPresenter().onBackInFragmentClick("history")
        }

        buttonActions.setOnClickListener {
            buttonHelp.setTextColor(Color.BLACK)
            buttonActions.setTextColor(ContextCompat.getColor(parentMvp.getCtx(),R.color.colorPrimaryNew))
            mPresenter.onActionsButtonClick()
        }
        buttonHelp.setOnClickListener {
            buttonHelp.setTextColor(ContextCompat.getColor(parentMvp.getCtx(),R.color.colorPrimaryNew))
            buttonActions.setTextColor(Color.BLACK)
            mPresenter.onHelpButtonClick()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("HistoryFragment","onStart")
        mPresenter.onAttach(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onDetach()
    }


    override fun updateAll() {
    }

    override fun clearList(type: String) {
        mListAdapter?.clear(type)
    }

    private fun initList(){
        listHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listHistory.layoutManager = layoutManager

        mListAdapter = HistoryAdapter(ArrayList(),parentMvp)
        listHistory.adapter = mListAdapter
    }

    override fun updateActions(action: ArrayList<Model1>, type: String) {
        mListAdapter?.type = type
        mListAdapter?.list?.addAll(action)
        mListAdapter?.notifyDataSetChanged()
    }

    override fun updateActions(action: Model1) {
        Log.d("updateActions",action.message)
        mListAdapter?.list?.add(action)
        mListAdapter?.notifyDataSetChanged()
    }

    override fun showEndPingBar(ping: Ping) {
        parentMvp.showEndPingBar(ping)
    }

    override fun showProgress() {
        progressHistory?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressHistory?.visibility = View.GONE
    }
}