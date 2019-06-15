package com.racjonalnytraktor.findme3.data.repository.base

interface Mapper<From, To> {
    fun map(item: From): To }