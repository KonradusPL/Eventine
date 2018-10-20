package com.racjonalnytraktor.findme3.ui.map.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Header
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.ui.adapters.HistoryAdapter
import com.racjonalnytraktor.findme3.ui.adapters.ManageAdapter
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.ManageGroupAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.manage.ManageMvp
import com.racjonalnytraktor.findme3.ui.manage.ManagePresenter
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.fragments.manage.SwipeHelper
import kotlinx.android.synthetic.main.dialog_new_subgroup.view.*
import kotlinx.android.synthetic.main.fragment_group_manager.*

class ManageGroupFragment<V: MapMvp.View>: BaseFragment<V>(),ManageMvp.View {

    private lateinit var mListAdapter: ManageAdapter
    private val mPresenter = ManagePresenter<ManageMvp.View>()


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

        buttonRefreshManage.setOnClickListener {
            mPresenter.getPeopleInGroups()
        }

        iconAddSubGroup.setOnClickListener {
            createDialog()
        }
        iconAddSubGroup.setImageDrawable(IconicsDrawable(parentContext)
                .icon(FontAwesome.Icon.faw_plus_square)
                .sizeDp(24)
                .color(ContextCompat.getColor(parentContext,R.color.white)))

        initList()

        mPresenter.onAttach(this)
    }

    private fun initList(){
        listGroups.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(parentContext)
        listGroups.layoutManager = layoutManager

        val swipeHelper = SwipeHelper()
        val touchHelper = ItemTouchHelper(swipeHelper)
        touchHelper.attachToRecyclerView(listGroups)

        mListAdapter = ManageAdapter(ArrayList(),this,touchHelper)
        swipeHelper.contract = mListAdapter
        listGroups.adapter = mListAdapter
    }

    private fun createDialog(){
        val root = LayoutInflater.from(parentContext).inflate(R.layout.dialog_new_subgroup,null)
        val dialog = AlertDialog.Builder(parentContext)
                .setView(root)
                .setTitle("Dodaj nową podgrupę")
                .setPositiveButton("Dodaj") { _, _ ->
                    val header = Header(root.fieldSubGroup.text.toString())
                    root.fieldSubGroup.text.clear()
                    header.type = "header"
                    mListAdapter.update(header)
                }.create()
        dialog.show()
    }

    override fun getPresenter(): ManagePresenter<ManageMvp.View> {
        return mPresenter
    }

    override fun updateList(item: Typed) {
        mListAdapter.update(item)
    }

    override fun showLoading() {
        buttonRefreshManage?.visibility = View.GONE
        progressManage?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressManage?.visibility = View.GONE
    }

    override fun showError() {
        buttonRefreshManage?.visibility = View.VISIBLE
    }
}