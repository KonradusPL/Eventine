package com.racjonalnytraktor.findme3.ui.map.fragments.addtask

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.ManageGroupAdapter
import com.racjonalnytraktor.findme3.ui.adapters.manage.Worker
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.map.MapMvp
import com.racjonalnytraktor.findme3.ui.map.listeners.Listener
import kotlinx.android.synthetic.main.add_task_users.*

class AddTaskUsersFragment<V: MapMvp.View>: BaseFragment<V>(), Listener.AddTaskList {

    private var mListAdapter: ManageGroupAdapter? = null
    lateinit var parentListener: UsersListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_task_users,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iconBack.setImageDrawable(IconicsDrawable(parentMvp.getCtx())
                .sizeDp(18)
                .color(Color.BLACK)
                .icon(GoogleMaterial.Icon.gmd_arrow_back))
        iconBack.setOnClickListener {
            parentListener.onBackArrowPressed()
        }

        buttonRefresh.setOnClickListener {
            parentMvp.getPresenter().onAddTaskListAttach(this)
        }


        if(mListAdapter?.itemCount ?: 0 == 0)
            parentMvp.getPresenter().onAddTaskListAttach(this)
        else
            showList()
    }

    override fun showList(list: ArrayList<Job>) {
        val arrayList = ArrayList<Job>()
        arrayList.addAll(list)
        mListAdapter = ManageGroupAdapter(arrayList,parentMvp,"addTask")

        listGroups.layoutManager = LinearLayoutManager(parentMvp.getCtx())
        listGroups.adapter = mListAdapter
    }

    private fun showList(){
        if (mListAdapter != null){
            listGroups.layoutManager = LinearLayoutManager(parentMvp.getCtx())
            listGroups.adapter = mListAdapter
        }
    }


    override fun showListLoading() {
        progressUsers?.visibility = View.VISIBLE
        buttonRefresh?.visibility = View.GONE
    }

    override fun hideListLoading() {
        progressUsers?.visibility = View.GONE
    }

    override fun showError() {
        buttonRefresh?.visibility = View.VISIBLE
    }

    fun getList(): ArrayList<String>{
        return mListAdapter?.getSelectedWorkers() ?: ArrayList()
    }

}