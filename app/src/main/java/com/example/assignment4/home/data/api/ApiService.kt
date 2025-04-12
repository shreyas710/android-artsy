package com.example.assignment4.home.data.api

import com.example.assignment4.home.data.models.Artist
import com.example.assignment4.home.data.models.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtsyApiService {
    // Fetch an XAPP token using your client_id and client_secret.
    @GET("api/artsy")
    suspend fun getXappToken(
    ): TokenResponse

    @GET("api/artsy/search_artist/{query}")
    suspend fun getArtists(
        @Path("query") query: String
    ): List<Artist>
}
