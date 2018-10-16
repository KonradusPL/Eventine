package com.racjonalnytraktor.findme3.data.network.model

import com.racjonalnytraktor.findme3.data.model.User

data class Help(
        var caller: User = User(),
        var called: ArrayList<String> = ArrayList(),
        var accepted: ArrayList<String> = ArrayList(),
        var declined: ArrayList<String> = ArrayList()
)