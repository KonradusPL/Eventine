package com.racjonalnytraktor.findme3.domain.usecase

import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import com.racjonalnytraktor.findme3.data.repository.auth.AuthApi
import com.racjonalnytraktor.findme3.data.repository.auth.AuthRepository
import com.racjonalnytraktor.findme3.data.repository.base.LocalRepository
import com.racjonalnytraktor.findme3.data.repository.user.UserRepository
import com.racjonalnytraktor.findme3.domain.base.CompletableUseCase
import com.racjonalnytraktor.findme3.domain.mappers.UserModelToUser
import com.racjonalnytraktor.findme3.ui.model.UserModel
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.realm.annotations.Index
import javax.inject.Inject
import javax.inject.Named

class LoginUseCase @Inject constructor(@Named("onSubscribe") subscribeScheduler: Scheduler,
                                       @Named("onObserve") observeScheduler: Scheduler,
                                       private val mAuthApi: AuthRepository,
                                       private val mUserLocalRepository: LocalRepository<User>)
    : CompletableUseCase<UserModel>(subscribeScheduler, observeScheduler){

    private val TAG = "LoginUseCase"

    private val toUserMapper = UserModelToUser()

    override fun buildUseCaseCompletable(request: UserModel?): Completable {
        val user = toUserMapper.map(request!!)

        return mAuthApi.loginUser(user)
                .map{response: LoginResponse -> joinUserWithResponse(user,response) }
                .flatMapCompletable { u: User -> mUserLocalRepository.add(u)}
    }

    private fun joinUserWithResponse(user: User, loginResponse: LoginResponse): User{
        user.token = loginResponse.token
        user.isPartner = loginResponse.isPartner

        return user
    }
}