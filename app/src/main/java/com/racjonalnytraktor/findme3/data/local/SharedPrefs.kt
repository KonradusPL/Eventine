package com.racjonalnytraktor.findme3.data.local

import android.content.Context
import androidx.content.edit


class SharedPrefs(context: Context): Prefs {

    private val CURRENT_USER_TOKEN = "CURRENT_USER_TOKEN"
    private val CURRENT_USER_EMAIL = "CURRENT_USER_EMAIL"
    private val CURRENT_USER_PASSWORD = "CURRENT_USER_PASSWORD"
    private val CURRENT_USER_FULLNAME = "CURRENT_USER_FULLNAME"
    private val CURRENT_USER_SIGNIN_MODE = "CURRENT_USER_SIGNIN_MODE"
    private val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"

    private val mSharedPrefs = context.getSharedPreferences("shared_preferences",Context.MODE_PRIVATE)

    override fun getUserToken(): String {
        return mSharedPrefs.getString(CURRENT_USER_TOKEN,"null")
    }

    override fun setUserToken(token: String) {
        mSharedPrefs.edit {
            putString(CURRENT_USER_TOKEN, token)
        }
    }

    override fun setUserFullName(fullName: String) {
        mSharedPrefs.edit {
            putString(CURRENT_USER_FULLNAME, fullName)
        }
    }

    override fun getUserFullName(): String {
        return mSharedPrefs.getString(CURRENT_USER_FULLNAME,"null")
    }

    override fun setUserEmail(email: String) {
        mSharedPrefs.edit {
            putString(CURRENT_USER_EMAIL, email)
        }
    }

    override fun getUserEmail(): String {
        return mSharedPrefs.getString(CURRENT_USER_EMAIL,"null")
    }

    override fun isUserLoggedIn(): Boolean {
        return mSharedPrefs.getBoolean(IS_USER_LOGGED_IN,false)
    }

    override fun setIsUserLoggedIn(value: Boolean) {
        mSharedPrefs.edit {
            putBoolean(IS_USER_LOGGED_IN,value)
        }
    }

}