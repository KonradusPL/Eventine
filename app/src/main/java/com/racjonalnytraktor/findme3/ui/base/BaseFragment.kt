package com.racjonalnytraktor.findme3.ui.base

import android.content.Context
import android.support.v4.app.Fragment

open class BaseFragment<V: MvpView>: Fragment(),MvpView {

    lateinit var parentActivity: V

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        parentActivity = context as V

    }

    override fun isConnectedToNetwork(): Boolean {
        return parentActivity.isConnectedToNetwork()
    }

    override fun showLoading() {
        parentActivity.showLoading()
    }

    override fun hideLoading() {
        parentActivity.hideLoading()
    }

    override fun hideKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        parentActivity.showMessage(message)
    }
    override fun showMessage(message: Int) {
        parentActivity.showMessage(message)
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