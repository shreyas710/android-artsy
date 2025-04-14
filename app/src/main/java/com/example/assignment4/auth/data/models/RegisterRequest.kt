package com.example.assignment4.auth.data.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
