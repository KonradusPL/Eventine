package com.racjonalnytraktor.findme3.data.repository

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.network.model.UserSimple

object ApplicationRepository {

    val groups = ArrayList<Group>()

    private val mMembers = ArrayList<UserSimple>()

    fun updateMembers(list: ArrayList<UserSimple>){
        mMembers.clear()
        mMembers.addAll(list)
    }

    fun getMembers(): ArrayList<UserSimple>{
        return mMembers
    }

}