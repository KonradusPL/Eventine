package com.racjonalnytraktor.findme3.ui.main.fragments.join

import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface JoinMvp {
    interface View: MvpView{
        fun updateList(invitation: Invitation)
        fun onInvitationClick(groupId: String)
        fun showJoinLoading()
        fun hideJoinLoading()
        fun openMapActivity()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onJoinGroupClick(groupName: String)
        fun onAcceptInvitationClick(groupId: String)
    }
}