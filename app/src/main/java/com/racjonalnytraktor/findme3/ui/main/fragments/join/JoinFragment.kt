package com.racjonalnytraktor.findme3.ui.main.fragments.join

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.ui.adapters.InvitationsAdapter
import com.racjonalnytraktor.findme3.ui.base.BaseFragment
import com.racjonalnytraktor.findme3.ui.main.MainMvp
import com.racjonalnytraktor.findme3.ui.map.MapActivity
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

        RxView.clicks(buttonJoin)
                .subscribe {
                    mPresenter.onJoinGroupClick(fieldGroupCode.text.toString())
                }
    }

    override fun updateList(invitation: Invitation) {
        mListAdapter.addItem(invitation)
    }

    override fun onInvitationClick(groupId: String) {
        mPresenter.onAcceptInvitationClick(groupId)
    }

    override fun showJoinLoading() {
        progressJoinGroup.visibility = View.VISIBLE
        progressJoinGroup.isIndeterminate = true
        fieldGroupCode.isEnabled = false
        buttonJoin.isEnabled = false
    }

    override fun hideJoinLoading() {
        progressJoinGroup.isIndeterminate = false
        progressJoinGroup.visibility = View.INVISIBLE
        fieldGroupCode.isEnabled = true
        buttonJoin.isEnabled = true
    }

    override fun openMapActivity() {
        startActivity(Intent(getCtx(), MapActivity::class.java))
    }

    private fun initList(){
        listInvitations.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        listInvitations.layoutManager = layoutManager

        mListAdapter = InvitationsAdapter(ArrayList(),this)
        listInvitations.adapter = mListAdapter
    }
}