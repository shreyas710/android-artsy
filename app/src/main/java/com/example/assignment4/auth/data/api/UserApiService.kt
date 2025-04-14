package com.example.assignment4.auth.data.api

import com.example.assignment4.auth.data.models.Favorite
import com.example.assignment4.auth.data.models.FavoriteRequest
import com.example.assignment4.auth.data.models.LoginRequest
import com.example.assignment4.auth.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

data class FavoriteResponse(
    val message: String
)

data class AddFavoriteResponse(
    val favorites: List<Favorite>
)

interface UserApiService {
    @POST("api/users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @HTTP(method = "DELETE", path = "api/users/favorites", hasBody = true)
    suspend fun deleteFavorite(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    ): Response<FavoriteResponse>

    @POST("api/users/favorites")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    ): Response<AddFavoriteResponse>
}