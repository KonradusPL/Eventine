package com.racjonalnytraktor.findme3.ui.manage

import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface ManageMvp {
    interface View: MvpView{
        fun updateList(item: Typed)
        fun showLoading()
        fun hideLoading()
        fun showError()
        fun getPresenter(): ManagePresenter<ManageMvp.View>
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onGroupChanged(changedGroup: String, changingId: String)
    }
}