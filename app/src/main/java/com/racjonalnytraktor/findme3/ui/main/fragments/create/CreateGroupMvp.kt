package com.racjonalnytraktor.findme3.ui.main.fragments.create

import com.racjonalnytraktor.findme3.data.model.Person
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface CreateGroupMvp {
    interface View: MvpView{
        fun updateList(user: User)
        fun hideCreateGroupLoading()
        fun showCreateGroupLoading()
        fun showFriendsLoading()
        fun hideFriendsLoading()
        fun openMapActivity()
        fun clearFriendsList()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun createEvent(groupName: String, friendsList: List<User>)
    }
}