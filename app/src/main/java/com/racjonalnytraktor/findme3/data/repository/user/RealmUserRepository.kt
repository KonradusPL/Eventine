package com.racjonalnytraktor.findme3.data.repository.user

import com.facebook.internal.Utility
import com.racjonalnytraktor.findme3.data.local.model.UserRealm
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.base.LocalRepository
import com.racjonalnytraktor.findme3.data.repository.base.ToUserMapper
import io.reactivex.Completable
import io.reactivex.Single

class RealmUserRepository: LocalRepository<User> {

    private val toUserMapper = ToUserMapper()

    override fun add(item: User): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(item: User): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(item: User): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(): Single<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}