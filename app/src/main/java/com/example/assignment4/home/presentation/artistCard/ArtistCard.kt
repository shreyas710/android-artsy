package com.example.assignment4.home.presentation.artistCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.data.models.Artist
import com.example.assignment4.R
import com.example.assignment4.auth.data.models.FavoriteRequest
import com.example.assignment4.auth.data.models.Favorites
import com.example.assignment4.core.data.api.LoginDataStoreManager
import com.example.assignment4.core.data.api.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ArtistCard(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    artist: Artist,
    snackbarHostState: SnackbarHostState,
    similarArtists: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    suspend fun addFavorite(favoriteId: String) {
        try {
            val token = "Bearer ${sharedViewModel.user.value!!.token}"
            val request = FavoriteRequest(id = favoriteId)
            val response = RetrofitInstance.userApi.addFavorite(
                token = token,
                request = request
            )
            if (response.isSuccessful) {
                sharedViewModel.user.value!!.favorites = response.body()!!.favorites
                val response2 = RetrofitInstance.artsyApi.getArtist(favoriteId)

                val createdAt = sharedViewModel.user.value!!.favorites.find {
                    it.id == favoriteId
                }!!.createdAt
                sharedViewModel.userFavorite.value = sharedViewModel.userFavorite.value + Favorites(
                    artist = response2,
                    createdAt = createdAt
                )
                LoginDataStoreManager.saveLoginResponse(context, sharedViewModel.user.value!!)
                snackbarHostState.showSnackbar("Added to Favorites")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeFavorite(favoriteId: String) {
        try {
            val token = "Bearer ${sharedViewModel.user.value!!.token}"
            val request = FavoriteRequest(id = favoriteId)
            val response = RetrofitInstance.userApi.deleteFavorite(
                token = token,
                request = request
            )
            if (response.isSuccessful) {
                sharedViewModel.user.value!!.favorites =
                    sharedViewModel.user.value!!.favorites.filter {
                        it.id != favoriteId
                    }
                sharedViewModel.userFavorite.value = sharedViewModel.userFavorite.value.filter {
                    it.artist.id != favoriteId
                }
                LoginDataStoreManager.saveLoginResponse(context, sharedViewModel.user.value!!)
                snackbarHostState.showSnackbar("Removed from Favorites")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchArtistDetails() {
        val response = RetrofitInstance.artsyApi.getArtist(artist.links.self.href.split("/").last())
        sharedViewModel.artistDetails.value = response
    }

    Card(
        onClick = {
            coroutineScope.launch {
                sharedViewModel.selectedArtist.value = artist
                fetchArtistDetails()
                navController.navigate("artistDetails")
            }
        },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 10.dp),
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(
                    model = if ("missing_image.png" in artist.links.thumbnail.href) {
                        R.drawable.artsy_logo
                    } else {
                        artist.links.thumbnail.href.trim()
                    }
                ),
                contentDescription = if (similarArtists) artist.name else artist.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            if (sharedViewModel.user.value != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.TopEnd)
                ) {
                    IconButton(onClick = {
                        if (sharedViewModel.userFavorite.value.any {
                                it.artist.id == artist.links.self.href.split(
                                    "/"
                                ).last()
                            }) {
                            coroutineScope.launch {
                                removeFavorite(
                                    artist.links.self.href.split(
                                        "/"
                                    ).last()
                                )
                            }
                        } else {
                            coroutineScope.launch {
                                addFavorite(
                                    artist.links.self.href.split(
                                        "/"
                                    ).last()
                                )
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (sharedViewModel.userFavorite.value.any {
                                    it.artist.id == artist.links.self.href.split(
                                        "/"
                                    ).last()
                                }) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorite Star",
                            modifier = Modifier.size(25.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (similarArtists) artist.name!! else artist.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier.weight(1f),
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Details",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}
