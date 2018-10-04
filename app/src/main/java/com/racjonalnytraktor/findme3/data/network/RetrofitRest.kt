package com.racjonalnytraktor.findme3.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitRest {

    private val BASE_URL = "http://35.242.212.126"

        private var gson = GsonBuilder()
                .setLenient()
                .create()

        private val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val networkService = retrofit.create(Routes::class.java)
}