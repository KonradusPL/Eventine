package com.racjonalnytraktor.findme3.data.repository.auth

import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Single<RegisterResponse>
}