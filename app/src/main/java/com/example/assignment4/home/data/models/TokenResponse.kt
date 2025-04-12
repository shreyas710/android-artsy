package com.example.assignment4.home.data.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("message")
    val message: String,
)