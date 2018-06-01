package com.racjonalnytraktor.findme3.ui.map

import com.racjonalnytraktor.findme3.ui.base.MvpPresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

interface MapMvp {
    interface View: MvpView{
        fun changeCreateGroupFragment()
    }
    interface Presenter<V: View>: MvpPresenter<V>{
        fun onNextButtonClick(task: String, descr: String)
    }
}