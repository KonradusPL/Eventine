package com.racjonalnytraktor.findme3.domain.mappers

import com.facebook.internal.Utility
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.base.Mapper
import com.racjonalnytraktor.findme3.ui.model.UserModel

class UserModelToUser: Mapper<UserModel,User> {

    override fun map(item: UserModel): User {
        val user = User()
        user.email = item.email
        user.password = item.password
        user.fullName = item.fullName
        return user
    }

}