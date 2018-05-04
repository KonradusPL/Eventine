package com.racjonalnytraktor.findme3.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import com.racjonalnytraktor.findme3.utils.DeviceInfo
import org.jetbrains.anko.toast


open class BaseActivity : AppCompatActivity(),MvpView{

    protected lateinit var mProgress: ProgressBar
    private lateinit var mDeviceInfo: DeviceInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDeviceInfo = DeviceInfo(this)
    }

    override fun showLoading() {
        if(::mProgress.isInitialized){
            hideLoading()
            mProgress.isIndeterminate = true
            mProgress.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        if(::mProgress.isInitialized){
            mProgress.isIndeterminate = false
            mProgress.visibility = View.INVISIBLE
        }
    }

    override fun hideKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: Int) {
        toast(message)
    }

    override fun showMessage(message: String) {
        toast(message)
    }

    override fun isConnectedToNetwork(): Boolean {
        return mDeviceInfo.isConnected()
    }

    override fun hasPermission(permission: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun increaseAlpha() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decreaseAlpha() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}