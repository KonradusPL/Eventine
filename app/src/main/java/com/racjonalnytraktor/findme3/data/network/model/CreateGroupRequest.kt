package com.racjonalnytraktor.findme3.data.network.model

data class CreateGroupRequest(
        val groupName: String,
        val facebookIds: List<String>,
        val normalIds: List<String>)
{
    override fun toString(): String{
        return "groupName: $groupName, facebookIds: $facebookIds, normalIds: $normalIds"
    }
}