package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Single

object RegisterRepository: BaseRepository() {

    fun registerUser(registerRequest: RegisterRequest): Single<RegisterResponse>{
        return rest.networkService.register(registerRequest)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}