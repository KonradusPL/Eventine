package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.ui.adapters.GroupsListAdapter
import com.racjonalnytraktor.findme3.ui.adapters.TasksListAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import kotlinx.android.synthetic.main.fragment_groups.*
import kotlinx.android.synthetic.main.fragment_groups.view.*

class FeedFragment<V: MainMvp.View>: BaseFragment<V>(), FeedMvp.View {

    lateinit var mGroupsListAdapter: GroupsListAdapter
    lateinit var mTasksListAdapter: TasksListAdapter
    lateinit var mPresenter: FeedPresenter<FeedMvp.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = FeedPresenter()
        mPresenter.onAttach(this)

        initGroupsList()
        initTasksList()
    }

    private fun initGroupsList(){
        listGroups.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        listGroups.layoutManager = layoutManager

        mGroupsListAdapter = GroupsListAdapter(ArrayList(),this)
        listGroups.adapter = mGroupsListAdapter
    }

    private fun initTasksList(){
        listTasks.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listTasks.layoutManager = layoutManager

        mTasksListAdapter = TasksListAdapter(ArrayList(),this.activity!!.applicationContext)
        listTasks.adapter = mTasksListAdapter
    }

    override fun updateGroupsList(group: Group) {
        mGroupsListAdapter.addItem(group)
    }

    override fun updateTasksList(task: Task) {
        mTasksListAdapter.addItem(task)
    }

    override fun showGroupsLoading() {
        hideGroupsLoading()
        textLoadinGroups.bringToFront()
        progressGroups.isIndeterminate = true
        progressGroups.visibility = View.VISIBLE
    }

    override fun hideGroupsLoading() {
        progressGroups.isIndeterminate = false
        progressGroups.visibility = View.VISIBLE
    }

    override fun openMapActivity(groupName: String) {
        startActivity(Intent(parentContext,MapActivity::class.java))
    }

    override fun onGroupsItemClick(groupName: String) {
        openMapActivity(groupName)
    }
}