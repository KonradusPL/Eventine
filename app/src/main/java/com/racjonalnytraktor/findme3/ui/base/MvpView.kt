package com.racjonalnytraktor.findme3.ui.base

interface MvpView {

    fun showLoading()
    fun hideLoading()

    fun hideKeyboard()

    fun showMessage(message: String)
    fun showMessage(message: Int)

    fun hasPermission(permission: String): Boolean
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int)

    fun increaseAlpha()
    fun decreaseAlpha()

    fun isConnectedToNetwork(): Boolean
}