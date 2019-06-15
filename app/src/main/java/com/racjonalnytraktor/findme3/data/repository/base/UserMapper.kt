package com.racjonalnytraktor.findme3.data.repository.base

import com.racjonalnytraktor.findme3.data.local.model.UserRealm
import com.racjonalnytraktor.findme3.data.model.User

class ToUserMapper: Mapper<UserRealm, User> {
    override fun map(item: UserRealm): User {
        val user =  User()
        user.email = item.email
        user.fullName = item.fullName
        user.facebookId = item.facebookId
        user.token = item.token
        user.profileUri = item.profileImage
        return user
    }
}