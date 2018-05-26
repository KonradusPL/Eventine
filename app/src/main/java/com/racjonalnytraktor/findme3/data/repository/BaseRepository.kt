package com.racjonalnytraktor.findme3.data.repository

import android.content.Context
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.facebook.FacebookNetwork

open class BaseRepository {
    protected val mRest = RetrofitRest()
    protected lateinit var mPrefs: SharedPrefs
    protected val mFacebookNetwork = FacebookNetwork()

    fun onAttatch(context: Context){
        mPrefs = SharedPrefs(context)
    }
}