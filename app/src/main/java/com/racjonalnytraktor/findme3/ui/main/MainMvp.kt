package com.racjonalnytraktor.findme3.ui.main

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface MainMvp {
    interface View: MvpView{
        fun changeProfileIcon(url: String)
        fun openLoginActivity()
        fun openMainActivity()
        fun setUpLeftNavigation(groups: ArrayList<Group>)
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun sendNotifToken(token: String)
        fun onLogoutButtonClick()
        fun onChangeGroupClick(groupName: String)
    }
}