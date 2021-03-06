package com.racjonalnytraktor.findme3.data.local.migration

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import io.realm.RealmSchema



class MyMigration: RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        if (oldVersion == 0L) {
            schema.get("UserRealm")?.addField("role", String::class.java,FieldAttribute.REQUIRED)
        }
        if(oldVersion == 1L){
            schema.get("UserRealm")?.addField("isPartner", Boolean::class.java,FieldAttribute.REQUIRED)
        }
        if(oldVersion == 2L){
            schema.get("UserRealm")?.addField("isSilentNotification", Boolean::class.java,FieldAttribute.REQUIRED)
        }
    }
}