package com.racjonalnytraktor.findme3.data.repository.base

import io.reactivex.Completable
import io.reactivex.Single

interface LocalRepository<T> {
    fun add(item: T): Completable
    fun remove(item: T): Completable
    fun update(item: T): Completable
    fun get(): Single<List<T>>
}