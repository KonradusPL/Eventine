package com.racjonalnytraktor.findme3.data.network.model.createping

/**
 * Created by Admin on 2018-06-01.
 */
data class CreatePingRequest(
        val groupId: String,
        val creator: String,
        val title: String,
        val desc: String,
        val geo: List<Double>,
        val ended: Boolean
)