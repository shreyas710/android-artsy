package com.example.assignment4.auth.data.api

import com.example.assignment4.auth.data.models.LoginRequest
import com.example.assignment4.auth.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("api/users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}