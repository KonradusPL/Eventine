package com.racjonalnytraktor.findme3.ui.main.fragments.join

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.ui.adapters.InvitationsAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import kotlinx.android.synthetic.main.fragment_join_group.*

class JoinFragment<V: MainMvp.View>: BaseFragment<V>(),JoinMvp.View {

    lateinit var mPresenter: JoinPresenter<JoinMvp.View>
    lateinit var mListAdapter: InvitationsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_join_group,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

        mPresenter = JoinPresenter()
        mPresenter.onAttach(this)
    }

    override fun updateList(invitation: Invitation) {
        mListAdapter.addItem(invitation)
    }

    private fun initList(){
        listInvitations.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listInvitations.layoutManager = layoutManager

        mListAdapter = InvitationsAdapter(ArrayList(),this.activity!!.applicationContext)
        listInvitations.adapter = mListAdapter
    }
}