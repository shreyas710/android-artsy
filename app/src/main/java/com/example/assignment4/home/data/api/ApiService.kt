package com.example.assignment4.home.data.api

import com.example.assignment4.home.data.models.TokenResponse
import retrofit2.http.GET

interface ArtsyApiService {
    // Fetch an XAPP token using your client_id and client_secret.
    @GET("api/artsy")
    suspend fun getXappToken(
    ): TokenResponse
}
