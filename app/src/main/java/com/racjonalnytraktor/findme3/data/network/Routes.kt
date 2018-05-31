package com.racjonalnytraktor.findme3.data.network

import com.racjonalnytraktor.findme3.data.network.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Routes {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>

    @POST("auth/social")
    fun registerByFacebook(@Body registerRequest: RegisterFbRequest): Single<RegisterFbResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Single<RegisterResponse>

    @POST("group/create")
    fun createGroup(@Header("X-Token") token: String, @Body request: CreateGroupRequest): Single<String>
}