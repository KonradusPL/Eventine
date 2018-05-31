package com.racjonalnytraktor.findme3.data.model

import com.google.gson.annotations.SerializedName


data class Invitation(@SerializedName("name") val groupName: String,
                      @SerializedName("inviter") val invitingPerson: String,
                      @SerializedName("url") val imageUri: String,
                      @SerializedName("id") val id: String)