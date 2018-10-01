package com.racjonalnytraktor.findme3

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import org.junit.Test
import java.util.*

class RestTest {

    val rest = RetrofitRest().networkService


    @Test
    fun registerTest(){


        val registerRequest = RegisterRequest("marcinmichno@wp.pl",
                "Marcin Michno","Michno123")
        rest.register(registerRequest)
                .subscribe({t ->
                    assert(true)
                    Log.d("registermichno",t.succes)
                },{t: Throwable? ->
                    assert(false)
                    Log.d("registermichno",t?.message.orEmpty())
                })
    }

    @Test
    fun loginTest(){

        val request = LoginRequest("marcinmichno@wp.pl",
                "Michno12s3")
        rest.login(request)
                .subscribe({t ->
                    assert(true)
                    Log.d("registermichno",t.token)
                },{t: Throwable? ->
                    assert(false)
                    Log.d("registermichno",t?.message.orEmpty())
                })
    }

}