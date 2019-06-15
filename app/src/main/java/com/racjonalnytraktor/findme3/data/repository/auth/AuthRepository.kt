package com.racjonalnytraktor.findme3.data.repository.auth

import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import io.reactivex.Single
import javax.inject.Inject

class AuthRepository @Inject constructor(private val mAuthApi: AuthApi) {

    fun loginUser(loginRequest: LoginRequest): Single<RegisterResponse>
            = mAuthApi.loginUser(loginRequest)

    fun registerUser(registerRequest: RegisterRequest): Single<LoginResponse>
            = mAuthApi.registerUser(registerRequest)
}