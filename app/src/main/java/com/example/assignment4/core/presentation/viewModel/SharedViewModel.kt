package com.example.assignment4.core.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.assignment4.auth.data.models.Favorites
import com.example.assignment4.auth.data.models.LoginResponse
import com.example.assignment4.home.data.models.Artist
import com.example.assignment4.home.data.models.Artwork
import com.example.assignment4.home.data.models.SelectedArtist

class SharedViewModel : ViewModel() {
    var token = mutableStateOf<String?>(null)
    var artists = mutableStateOf<List<Artist>>(emptyList())
    var selectedArtist = mutableStateOf<Artist?>(null)
    var artistDetails = mutableStateOf<SelectedArtist?>(null)
    var user = mutableStateOf<LoginResponse?>(null)
    var userFavorite = mutableStateOf<List<Favorites>>(emptyList())
    var artistArtworks = mutableStateOf<List<Artwork>>(emptyList())
}