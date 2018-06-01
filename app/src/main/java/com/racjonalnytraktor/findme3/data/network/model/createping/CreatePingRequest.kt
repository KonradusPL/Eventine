package com.racjonalnytraktor.findme3.data.network.model.createping

data class CreatePingRequest(
        val groupId: String,
        val creator: String,
        val title: String,
        val targetGroups: List<String>,
        val desc: String,
        val geo: List<Double>
)