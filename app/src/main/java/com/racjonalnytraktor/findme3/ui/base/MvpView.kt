package com.racjonalnytraktor.findme3.ui.base

import com.racjonalnytraktor.findme3.utils.PermissionsHelper
import io.reactivex.Completable
import io.reactivex.Observable

interface MvpView {

    fun showLoading()
    fun hideLoading()

    fun hideKeyboard()

    fun showMessage(message: String)
    fun showMessage(message: Int)

    fun checkPermission(permission: String): Observable<PermissionsHelper.PermissionState>
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int)

    fun checkLocationSettings(): Completable

    fun increaseAlpha()
    fun decreaseAlpha()

    fun isConnectedToNetwork(): Boolean
}