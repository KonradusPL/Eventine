package com.racjonalnytraktor.findme3.data.repository.auth

import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import io.reactivex.Single
import javax.inject.Inject

class AuthRepository @Inject constructor(private val mAuthApi: AuthApi) {

    fun loginUser(user: User): Single<LoginResponse>{
        val loginRequest = LoginRequest(user.email,user.password)
        return mAuthApi.loginUser(loginRequest)
    }

    fun registerUser(registerRequest: RegisterRequest): Single<RegisterResponse>
            = mAuthApi.registerUser(registerRequest)
}