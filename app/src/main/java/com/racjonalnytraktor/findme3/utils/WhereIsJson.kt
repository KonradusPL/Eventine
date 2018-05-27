package com.racjonalnytraktor.findme3.utils

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.model.UserFacebook
import org.json.JSONObject

object WhereIsJson {
    fun getUserBasic(jsonObject: JSONObject): User {
        Log.d("jsonik",jsonObject.toString())
        val name = jsonObject.get("name") as String
        val id = jsonObject.get("id") as String
        val picture = jsonObject.getJSONObject("picture")
                .getJSONObject("data")
                .getString("url")

        val user = User(id,picture,name)
        return user
    }

    fun getFriendsArray(jsonObject: JSONObject): ArrayList<UserFacebook>{
        val array = jsonObject.getJSONArray("data")
        val newArray = ArrayList<UserFacebook>()
        for(i in 0..array.length()-1){
            val jsonObject = array.getJSONObject(i)
            Log.d("asdasd",jsonObject.toString())
            val name = jsonObject.getString("name")
            val id = jsonObject.getString("id")
            newArray.add(UserFacebook(name,id))
        }
        return  newArray
    }
}