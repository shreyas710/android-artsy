package com.example.assignment4.home.data.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class SimpleCookieJar : CookieJar {
    private val cookieStore: MutableList<Cookie> = mutableListOf()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        // Add any cookies from the server response.
        cookieStore.addAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // Return cookies that match the request URL.
        return cookieStore.filter { it.matches(url) }
    }

    // Helper function to retrieve a cookie by name.
    fun getCookieValue(cookieName: String): String? {
        return cookieStore.find { it.name == cookieName }?.value
    }
}
