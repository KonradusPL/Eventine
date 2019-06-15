package com.racjonalnytraktor.findme3.ui.manage

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import com.jakewharton.rxbinding2.view.RxView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Header
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.ui.adapters.ManageAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.map.fragments.manage.SwipeHelper
import kotlinx.android.synthetic.main.activity_manage_sub_groups.*
import kotlinx.android.synthetic.main.dialog_new_subgroup.view.*

class ManageSubGroupsActivity : BaseActivity(),ManageMvp.View,ManageAdapter.Listener {
    override fun getPresenter(): ManagePresenter<ManageMvp.View> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var mPresenter: ManagePresenter<ManageMvp.View>
    lateinit var dialog: AlertDialog

    private lateinit var mListAdapter: ManageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_sub_groups)

        initList()
        setSupportActionBar(toolbarManage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createDialog()

        RxView.clicks(buttonAddSubGroup)
                .subscribe {
                    dialog.show()
                }

        mPresenter = ManagePresenter()
        mPresenter.onAttach(this)
    }

    private fun initList(){
        listManage.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        listManage.layoutManager = layoutManager

        val swipeHelper = SwipeHelper()
        val touchHelper = ItemTouchHelper(swipeHelper)
        touchHelper.attachToRecyclerView(listManage)

        mListAdapter = ManageAdapter(ArrayList(),this,touchHelper)
        swipeHelper.contract = mListAdapter
        listManage.adapter = mListAdapter
    }

    private fun createDialog(){
        val root = LayoutInflater.from(this).inflate(R.layout.dialog_new_subgroup,null)
        dialog = AlertDialog.Builder(this)
                .setView(root)
                .setTitle("Dodaj nową podgrupę")
                .setPositiveButton("Dodaj") { _, _ ->
                    val header = Header(root.fieldSubGroup.text.toString())
                    root.fieldSubGroup.text.clear()
                    header.type = "header"
                    mListAdapter.update(header)
                }.create()
    }

    override fun updateList(item: Typed) {
        mListAdapter.update(item)
    }

    override fun onGroupChanged(changedGroup: String,changingId: String) {
        mPresenter.onGroupChanged(changedGroup,changingId)
    }


}
