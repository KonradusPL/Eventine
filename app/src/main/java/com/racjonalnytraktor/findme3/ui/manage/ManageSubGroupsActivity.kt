package com.racjonalnytraktor.findme3.ui.manage

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.ui.adapters.ManageAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.manage.SwipeHelper
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

        val swipeHelper = SwipeHelper()
        val touchHelper = ItemTouchHelper(swipeHelper)
        touchHelper.attachToRecyclerView(listManage)

        mListAdapter = ManageAdapter(ArrayList(),this)
        swipeHelper.contract = mListAdapter
        listManage.adapter = mListAdapter
    }

    override fun updateList(item: Typed) {
        mListAdapter.update(item)
    }

    override fun onGroupChanged(changedGroup: String,changingId: String) {
        mPresenter.onGroupChanged(changedGroup,changingId)
    }


}
