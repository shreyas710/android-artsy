package com.example.assignment4.home.data.api

import com.example.assignment4.home.data.models.Artist
import com.example.assignment4.home.data.models.Artwork
import com.example.assignment4.home.data.models.GeneCategory
import com.example.assignment4.home.data.models.SelectedArtist
import com.example.assignment4.home.data.models.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtsyApiService {
    @GET("api/artsy")
    suspend fun getXappToken(
    ): TokenResponse

    @GET("api/artsy/search_artist/{query}")
    suspend fun getArtists(
        @Path("query") query: String
    ): List<Artist>

    @GET("api/artsy/get_artist/{id}")
    suspend fun getArtist(
        @Path("id") id: String
    ): SelectedArtist

    @GET("api/artsy/get_artist_artworks/{id}")
    suspend fun getArtistArtworks(
        @Path("id") id: String
    ): Response<List<Artwork>>

    @GET("api/artsy/get_artist_genes/{id}")
    suspend fun getArtistGenes(
        @Path("id") id: String
    ): Response<List<GeneCategory>>
}
