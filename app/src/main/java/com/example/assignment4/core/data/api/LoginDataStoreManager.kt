package com.example.assignment4.core.data.api

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.assignment4.auth.data.models.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

// Define the Preferences key
private val LOGIN_RESPONSE_KEY = stringPreferencesKey("login_response")

object LoginDataStoreManager {

    // Save LoginResponse to DataStore as JSON
    suspend fun saveLoginResponse(context: Context, loginResponse: LoginResponse) {
        val gson = Gson()
        val jsonString = gson.toJson(loginResponse)
        context.dataStore.edit { preferences ->
            preferences[LOGIN_RESPONSE_KEY] = jsonString
        }
    }

    // Retrieve LoginResponse from DataStore
    suspend fun getLoginResponse(context: Context): LoginResponse? {
        val gson = Gson()
        val preferences = context.dataStore.data.first()
        val jsonString = preferences[LOGIN_RESPONSE_KEY]
        return jsonString?.let {
            gson.fromJson(it, LoginResponse::class.java)
        }
    }

    // Optionally, clear the stored LoginResponse
    suspend fun clearLoginResponse(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(LOGIN_RESPONSE_KEY)
        }
    }
}
