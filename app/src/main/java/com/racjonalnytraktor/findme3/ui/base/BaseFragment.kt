package com.racjonalnytraktor.findme3.ui.base

import android.content.Context
import android.support.v4.app.Fragment

open class BaseFragment<V: MvpView>: Fragment(),MvpView {

    lateinit var parentMvp: V
    lateinit var parentContext: Context

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        parentMvp = context as V
        parentContext = context

    }

    override fun isConnectedToNetwork(): Boolean {
        return parentMvp.isConnectedToNetwork()
    }

    override fun showLoading() {
        parentMvp.showLoading()
    }

    override fun hideLoading() {
        parentMvp.hideLoading()
    }

    override fun hideKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        parentMvp.showMessage(message)
    }
    override fun showMessage(message: Int) {
        parentMvp.showMessage(message)
    }


    override fun hasPermission(permission: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decreaseAlpha(){
        view!!.alpha = 0.3f
    }
    override fun increaseAlpha(){
        view!!.alpha = 1f
    }




}