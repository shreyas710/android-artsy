package com.example.assignment4.core.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.assignment4.home.data.models.Artist

class SharedViewModel : ViewModel() {
    var token = mutableStateOf<String?>(null)
    var artists = mutableStateOf<List<Artist>>(emptyList())
    var selectedArtist = mutableStateOf<Artist?>(null)
}