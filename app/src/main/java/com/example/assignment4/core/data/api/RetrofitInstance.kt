package com.example.assignment4.core.data.api

import com.example.assignment4.home.data.api.ArtsyApiService
import com.example.assignment4.home.data.api.SimpleCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://shreyas710-cs571-assignment3.uc.r.appspot.com/"

    val cookieJar = SimpleCookieJar()

    private val okHttpClient = OkHttpClient.Builder().cookieJar(cookieJar).build()

    val api: ArtsyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtsyApiService::class.java)
    }
}