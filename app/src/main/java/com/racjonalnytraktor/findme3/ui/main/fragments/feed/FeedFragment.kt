package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.GroupWithUsers
import com.racjonalnytraktor.findme3.data.model.Task
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.ui.adapters.GroupsListAdapter
import com.racjonalnytraktor.findme3.ui.adapters.TasksListAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.fragment_groups.view.*
import kotlinx.android.synthetic.main.fragment_groups_new.*
import kotlinx.android.synthetic.main.fragment_join_group.*

class FeedFragment<V: MainMvp.View>: BaseFragment<V>(), FeedMvp.View {

    lateinit var mGroupsListAdapter: GroupsListAdapter
    lateinit var mTasksListAdapter: TasksListAdapter
    lateinit var mPresenter: FeedPresenter<FeedMvp.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups_new,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGroupsList()
        initTasksList()

        mPresenter = FeedPresenter()
        mPresenter.onAttach(this)
    }

    override fun updateTasks(ping: Ping) {
        mTasksListAdapter.addItem(ping)
    }

    private fun initGroupsList(){
        listGroups.setHasFixedSize(true)
        listGroups.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(activity)
        listGroups.layoutManager = layoutManager

        mGroupsListAdapter = GroupsListAdapter(ArrayList(),this)
        listGroups.adapter = mGroupsListAdapter
    }

    private fun initTasksList(){
        listTasks.setHasFixedSize(true)
        listTasks.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(activity)
        listTasks.layoutManager = layoutManager

        mTasksListAdapter = TasksListAdapter(ArrayList(),this.activity!!.applicationContext)
        listTasks.adapter = mTasksListAdapter
    }

    override fun updateGroupsList(group: GroupWithUsers) {
        mGroupsListAdapter.addItem(group)
    }

    override fun updateTasksList(task: Ping) {
        mTasksListAdapter.addItem(task)
    }

    override fun showGroupsLoading() {
        progressGroups?.isIndeterminate = true
        progressGroups?.visibility = View.VISIBLE
        //textEmptyGroups.visibility = View.INVISIBLE
    }

    override fun hideGroupsLoading() {
            if(progressGroups != null){
               progressGroups.isIndeterminate = false
               progressGroups.visibility = View.INVISIBLE
            }
    }

    override fun openMapActivity(groupName: String) {
        startActivity(Intent(parentContext,MapActivity::class.java))
    }

    override fun onGroupsItemClick(groupName: String,groupId: String) {
        mPresenter.onGroupItemClick(groupName,groupId)
        openMapActivity(groupName)
    }


    override fun hideTasksLoading() {
        progressTasks?.visibility = View.INVISIBLE
        if(mTasksListAdapter.itemCount == 0){
            //if (layoutNoTasks != null)
                //layoutNoTasks.visibility = View.VISIBLE
        }
    }

    override fun showTasksLoading() {
        progressTasks?.visibility = View.VISIBLE
        //if(layoutNoTasks != null)
         //layoutNoTasks.visibility = View.INVISIBLE
    }
}