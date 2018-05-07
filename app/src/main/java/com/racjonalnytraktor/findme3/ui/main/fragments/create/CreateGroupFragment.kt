package com.racjonalnytraktor.findme3.ui.main.fragments.create

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Friend
import com.racjonalnytraktor.findme3.ui.adapters.FriendsAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import kotlinx.android.synthetic.main.fragment_create_group.*
import android.support.v7.widget.LinearLayoutManager



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
    }

    override fun updateList(friend: Friend) {
        listAdapter.addItem(friend)
    }

    private fun initList(){
        listFriends.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listFriends.layoutManager = layoutManager

        listAdapter = FriendsAdapter(ArrayList(),this.activity!!.applicationContext)
        listFriends.adapter = listAdapter
    }
}