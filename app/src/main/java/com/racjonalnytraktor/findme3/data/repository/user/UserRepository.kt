package com.racjonalnytraktor.findme3.data.repository.user

import com.racjonalnytraktor.findme3.data.local.Preferences
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.base.LocalRepository
import io.reactivex.Completable
import javax.inject.Inject

class UserRepository @Inject constructor(private val mLocalRepository: LocalRepository<User>,
                                         private val mUserApi: UserApi) {


}