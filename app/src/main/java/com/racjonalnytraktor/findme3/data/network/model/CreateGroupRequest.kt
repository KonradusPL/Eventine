package com.racjonalnytraktor.findme3.data.network.model

data class CreateGroupRequest(
        val groupName: String,
        val facebookIds: List<String>,
        val normalIds: List<String>)