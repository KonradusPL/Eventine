package com.racjonalnytraktor.findme3.data.network

import com.racjonalnytraktor.findme3.data.network.model.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.RegisterFbRequest
import com.racjonalnytraktor.findme3.data.network.model.RegisterFbResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface Routes {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>

    @POST("auth/social")
    fun registerByFacebook(@Body registerRequest: RegisterFbRequest): Single<RegisterFbResponse>
}