package com.racjonalnytraktor.findme3.data.local.db

import io.realm.Realm

class RealmLocalDb: LocalDb {
    private val mRealm = Realm.getDefaultInstance()

}