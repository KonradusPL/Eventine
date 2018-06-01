package com.racjonalnytraktor.findme3.data.local

import android.content.Context
import android.util.Log
import androidx.content.edit
import com.racjonalnytraktor.findme3.data.model.User


class SharedPrefs(context: Context): Prefs {

    private val CURRENT_USER_TOKEN = "CURRENT_USER_TOKEN"
    private val CURRENT_USER_EMAIL = "CURRENT_USER_EMAIL"
    private val CURRENT_USER_PASSWORD = "CURRENT_USER_PASSWORD"
    private val CURRENT_USER_FULLNAME = "CURRENT_USER_FULLNAME"
    private val CURRENT_USER_SIGNIN_MODE = "CURRENT_USER_SIGNIN_MODE"
    private val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"
    private val CURRENT_USER_PROFILE_IMAGE = "PROFILE_IMAGE"
    private val CURRENT_USER_FB_ID = "CURRENT_USER_FB_ID"
    private val CURRENT_GROUP = "CURRENT_GROUP"


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

    override fun getUserProfileImage(): String {
        return mSharedPrefs.getString(CURRENT_USER_PROFILE_IMAGE,"null")
    }

    override fun setUserProfileImage(value: String) {
        mSharedPrefs.edit {
            putString(CURRENT_USER_PROFILE_IMAGE,value)
        }
    }

    override fun getCurrentUser(): User {
        return User(
                "",
                getUserProfileImage(),
                getUserFullName())
    }

    override fun setCurrentUser(value: User) {
        Log.d("user",value.facebookId +" "+  value.profileUri+ " "+ value.fullName)
        setUserFullName(value.fullName)
        setUserProfileImage(value.profileUri)
        setFacebookId(value.facebookId)
        setUserToken(value.token)
    }

    override fun setFacebookId(value: String) {
        mSharedPrefs.edit{
            putString(CURRENT_USER_FB_ID,value)
        }
    }

    override fun getFacebookId(): String {
        return mSharedPrefs.getString(CURRENT_USER_FB_ID,"null")
    }

    override fun setCurrentGroup(value: String) {
        mSharedPrefs.edit{
            putString(CURRENT_GROUP,value)
        }
    }

    override fun getCurrentGroup(): String {
        return mSharedPrefs.getString(CURRENT_GROUP,"null")
    }


}