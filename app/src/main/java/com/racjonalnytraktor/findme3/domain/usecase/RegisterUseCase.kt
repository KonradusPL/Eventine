package com.racjonalnytraktor.findme3.domain.usecase

import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import com.racjonalnytraktor.findme3.data.repository.auth.AuthRepository
import com.racjonalnytraktor.findme3.data.repository.base.LocalRepository
import com.racjonalnytraktor.findme3.domain.base.CompletableUseCase
import com.racjonalnytraktor.findme3.domain.mappers.UserModelToUser
import com.racjonalnytraktor.findme3.ui.model.UserModel
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class RegisterUseCase@Inject constructor(@Named("onSubscribe") subscribeScheduler: Scheduler,
                                         @Named("onObserve") observeScheduler: Scheduler,
                                         private val mAuthRepository: AuthRepository,
                                         private val mUserLocalRepository: LocalRepository<User>)
    : CompletableUseCase<UserModel>(subscribeScheduler, observeScheduler) {

    private val TAG = "LoginUseCase"

    private val toUserMapper = UserModelToUser()

    override fun buildUseCaseCompletable(request: UserModel?): Completable {
        val user = toUserMapper.map(request!!)

        return mAuthRepository.registerUser(user)
                .map{response: RegisterResponse -> joinUserWithResponse(user,response) }
                .flatMapCompletable { u: User -> mUserLocalRepository.add(u)}
    }

    private fun joinUserWithResponse(user: User, registerResponse: RegisterResponse): User{
        user.token = registerResponse.token
        return user
    }
}