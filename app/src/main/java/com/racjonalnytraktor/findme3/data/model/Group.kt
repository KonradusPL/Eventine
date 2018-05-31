package com.racjonalnytraktor.findme3.data.model

import com.google.gson.annotations.SerializedName

data class Group(@SerializedName("name") val groupName: String, val groupPictureUri: String, val id: String = "")