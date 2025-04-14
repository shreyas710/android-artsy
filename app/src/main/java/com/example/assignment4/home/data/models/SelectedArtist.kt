package com.example.assignment4.home.data.models

import com.google.gson.annotations.SerializedName

data class SelectedArtist(
    @SerializedName("biography")
    val biography: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("deathday")
    val deathday: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nationality")
    val nationality: String,
)