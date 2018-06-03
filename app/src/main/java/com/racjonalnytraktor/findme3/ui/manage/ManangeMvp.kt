package com.racjonalnytraktor.findme3.ui.manage

import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface ManangeMvp {
    interface View: MvpView{
        fun updateList(item: Typed)
    }
    interface Presenter<V: View>: MvpPresenter<V>{

    }
}