package com.example.assignment4.auth.data.models

import com.example.assignment4.home.data.models.SelectedArtist
import java.util.Date

data class Favorites(
    var artist: SelectedArtist,
    var createdAt: Date
)
