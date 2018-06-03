package com.racjonalnytraktor.findme3.ui.manage

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.ui.adapters.ManageAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_manage_sub_groups.*

class ManageSubGroupsActivity : BaseActivity(),ManangeMvp.View,ManageAdapter.Listener {

    lateinit private var mListAdapter: ManageAdapter
    lateinit var mPresenter: ManagePresenter<ManangeMvp.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_sub_groups)

        initList()

        mPresenter = ManagePresenter()
        mPresenter.onAttach(this)

        setSupportActionBar(toolbarManage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initList(){
        listManage.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        listManage.layoutManager = layoutManager

        mListAdapter = ManageAdapter(ArrayList(),this)
        listManage.adapter = mListAdapter
    }

    override fun updateList(item: Typed) {
        mListAdapter.update(item)
    }
}
