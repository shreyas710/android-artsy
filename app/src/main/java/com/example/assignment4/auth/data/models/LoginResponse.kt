package com.example.assignment4.auth.data.models

data class LoginResponse(
    val _id: String,
    val name: String,
    val email: String,
    val pic: String,
    val token: String,
    val favorites: List<Favorite>,
)
