package com.racjonalnytraktor.findme3.ui.map.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.ui.adapters.HistoryAdapter
import com.racjonalnytraktor.findme3.ui.adapters.InvitationsAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment<V: MapMvp.View>: BaseFragment<V>(),HistoryMvp.View {

    lateinit var mListAdapter: HistoryAdapter
    lateinit var mPresenter: HistoryPresenter<HistoryMvp.View>

    val listInfo = ArrayList<Model1>()
    val listHelp = ArrayList<Model1>()

    init {
        listHelp.add(Model1("Jan Kowalski","Wykonał Mateusz Zalewski","9 min temu"))
        listHelp.add(Model1("Mateusz Zalewski","Wykonał John Doe","18 min temu") )
        listHelp.add(Model1("Jan Kowalski","Wykonał John Doe","25 min temu"))
        listHelp.add(Model1("Ewelina Nowak","Wykonał Mateusz Zalewski","30 min temu"))
        listInfo.add(Model1("SZACHOWNICA","Musisz zanieść szachownice do drugiego pokoju. Stara ma zniszczone nawiasy.","9 min temu"))
        listInfo.add(Model1("WYMIANA BATERII","Musisz wymienić stare baterie w mikrofonach na 2 auli.","18 min temu") )
        listInfo.add(Model1("SPRAWDŹ NAGŁOŚNIENIE","Sprawdź nagłośnienie w małej salce przy punkcie u higienistki nieopodal portierni.","25 min temu"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_history,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = HistoryPresenter()
        mPresenter.onAttach(this)

        initList()

        iconArrow.setImageDrawable(IconicsDrawable(parentContext)
                .icon(GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                .sizeDp(24)
                .color(Color.BLACK))
        iconArrow.setOnClickListener {
            parentMvp.getPresenter().onBackInFragmentClick("history")
        }

        buttonInfo.setOnClickListener {
            buttonHelp.setTextColor(Color.BLACK)
            buttonInfo.setTextColor(ContextCompat.getColor(parentMvp.getCtx(),R.color.colorPrimaryNew))
            mListAdapter.list = listInfo
            mListAdapter.notifyDataSetChanged()
            //mPresenter.onInfoButtonClick()
        }
        buttonHelp.setOnClickListener {
            buttonHelp.setTextColor(ContextCompat.getColor(parentMvp.getCtx(),R.color.colorPrimaryNew))
            buttonInfo.setTextColor(Color.BLACK)
            mListAdapter.list = listHelp
            mListAdapter.notifyDataSetChanged()
        }
    }

    override fun updateAll() {
    }

    override fun clearList(type: String) {
        mListAdapter.clear(type)
    }

    private fun initList(){
        listHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listHistory.layoutManager = layoutManager

        mListAdapter = HistoryAdapter(listInfo,mPresenter,parentMvp as Context)
        listHistory.adapter = mListAdapter
    }

    override fun showEndPingBar(ping: Ping) {
        parentMvp.showEndPingBar(ping)
    }

    override fun showProgress() {
        progressHistory.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressHistory.visibility = View.GONE
    }
}