package com.racjonalnytraktor.findme3.ui.map.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = HistoryPresenter()
        mPresenter.onAttach(this)
        initList()
    }

    override fun updatePings(ping: Ping) {
        mListAdapter.updatePings(ping)
    }

    override fun updateInfos(info: Info) {
        mListAdapter.updateInfo(info)
    }

    override fun updateAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearList(type: String) {
        mListAdapter.clear(type)
    }

    fun initList(){
        listHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listHistory.layoutManager = layoutManager

        mListAdapter = HistoryAdapter(arrayListOf(), arrayListOf())
        listHistory.adapter = mListAdapter
    }
}