package com.racjonalnytraktor.findme3.ui.base

import android.content.Context
import android.widget.ProgressBar
import com.racjonalnytraktor.findme3.utils.PermissionsHelper
import io.reactivex.Completable
import io.reactivex.Observable

interface MvpView {

    fun showLoading()
    fun hideLoading()

    fun hideKeyboard()

    fun showMessage(message: String, type: MessageType = MessageType.NORMAL)
    fun showMessage(message: Int, type: MessageType = MessageType.NORMAL)

    fun checkPermission(permission: String): Observable<PermissionsHelper.PermissionState>
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int)

    fun checkLocationSettings(): Completable

    fun increaseAlpha()
    fun decreaseAlpha()

    fun isConnectedToNetwork(): Boolean

    fun getCtx(): Context

    enum class MessageType{
        INFO,ERROR,NORMAL,SUCCESS
    }
}