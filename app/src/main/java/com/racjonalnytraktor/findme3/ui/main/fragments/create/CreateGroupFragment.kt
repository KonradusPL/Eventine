package com.racjonalnytraktor.findme3.ui.main.fragments.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.adapters.FriendsAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import kotlinx.android.synthetic.main.fragment_create_group.*
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.jakewharton.rxbinding2.view.RxView
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.ui.map.MapActivity


class CreateGroupFragment<V: MainMvp.View>: BaseFragment<V>(),CreateGroupMvp.View {

    lateinit var listAdapter: FriendsAdapter
    lateinit var presenter: CreateGroupPresenter<CreateGroupMvp.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_group,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        presenter = CreateGroupPresenter()
        presenter.onAttach(this)

        RxView.clicks(buttonCreate)
                .subscribe {
                    val checkedFriends = listAdapter.getCheckedFriends()
                    Log.d("checkedFriends",checkedFriends.size.toString())
                    presenter.createEvent(fieldGroupCode.text.toString(),checkedFriends)
                }
        RxView.clicks(buttonRefresh)
                .subscribe {
                    listAdapter.clearFriends()
                    presenter.getFriends()
                }
    }

    override fun showCreateGroupLoading() {
        progressCreateGroup.visibility = View.VISIBLE
        progressCreateGroup.isIndeterminate = true
        fieldGroupCode.isEnabled = false
        buttonCreate.isEnabled = false    }

    override fun hideCreateGroupLoading() {
        progressCreateGroup.isIndeterminate = false
        progressCreateGroup.visibility = View.INVISIBLE
        fieldGroupCode.isEnabled = true
        buttonCreate.isEnabled = true
        fieldGroupCode.text.clear()
        listAdapter.unCheckFriends()
    }

    override fun updateList(user: User) {
        listAdapter.addItem(user)
    }

    override fun openMapActivity() {
        startActivity(Intent(getCtx(),MapActivity::class.java))
    }

    private fun initList(){
        listFriends.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listFriends.layoutManager = layoutManager

        listAdapter = FriendsAdapter(ArrayList(),this.activity!!.applicationContext)
        listFriends.adapter = listAdapter
    }
}