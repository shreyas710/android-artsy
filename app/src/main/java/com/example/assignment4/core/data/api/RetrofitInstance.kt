package com.example.assignment4.core.data.api

import com.example.assignment4.auth.data.api.UserApiService
import com.example.assignment4.home.data.api.ArtsyApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://shreyas710-cs571-assignment3.uc.r.appspot.com/"

    val cookieJar = SimpleCookieJar()

    private val okHttpClient = OkHttpClient.Builder().cookieJar(cookieJar).build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val artsyApi: ArtsyApiService by lazy {
        retrofit.create(ArtsyApiService::class.java)
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}