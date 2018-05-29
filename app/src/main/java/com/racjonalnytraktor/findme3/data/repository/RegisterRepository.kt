package com.racjonalnytraktor.findme3.data.repository

import com.racjonalnytraktor.findme3.data.network.model.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.RegisterResponse
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.Single
import java.nio.channels.SelectableChannel

object RegisterRepository: BaseRepository() {

    fun registerUser(registerRequest: RegisterRequest): Single<RegisterResponse>{
        return rest.networkService.register(registerRequest)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
    }
}