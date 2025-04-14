package com.example.assignment4.home.presentation.artistDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.assignment4.auth.data.models.FavoriteRequest
import com.example.assignment4.auth.data.models.Favorites
import com.example.assignment4.core.data.api.LoginDataStoreManager
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.presentation.artworksScreen.ArtworksScreen
import com.example.assignment4.home.presentation.detailsScreen.DetailsScreen
import com.example.assignment4.home.presentation.similarArtistsScreen.SimilarArtistsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetails(navController: NavController, sharedViewModel: SharedViewModel) {
    val tabs = remember { mutableListOf("Details", "Artworks") }
    val icons = remember { mutableListOf(Icons.Default.Info, Icons.Default.AccountBox) }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    if (sharedViewModel.user.value != null && !tabs.contains("Similar Artists")) {
        tabs.add("Similar Artists")
    }

    if (sharedViewModel.user.value != null && !icons.contains(Icons.Default.PersonSearch)) {
        icons.add(Icons.Default.PersonSearch)
    }

    fun onTabSelected(index: Int) {
        selectedTabIndex = index
    }

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    suspend fun addFavorite(favoriteId: String) {
        try {
            val token = "Bearer ${sharedViewModel.user.value!!.token}"
            val request = FavoriteRequest(id = favoriteId)
            val response = RetrofitInstance.userApi.addFavorite(
                token = token,
                request = request
            )
            if (response.isSuccessful) {
                println("Favorite added successfully: ${response.body()}")
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            sharedViewModel.artistDetails.value!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                    }
                },
                actions = {
                    if (sharedViewModel.user.value != null) {
                        IconButton(onClick = {
                            if (sharedViewModel.userFavorite.value.any {
                                    it.artist.id == sharedViewModel.artistDetails.value!!.id
                                }) {
                                coroutineScope.launch {
                                    removeFavorite(
                                        sharedViewModel.artistDetails.value!!.id
                                    )
                                }
                            } else {
                                coroutineScope.launch {
                                    addFavorite(
                                        sharedViewModel.artistDetails.value!!.id
                                    )
                                }
                            }
                        }) {
                            Icon(
                                imageVector = if (sharedViewModel.userFavorite.value.any {
                                        it.artist.id == sharedViewModel.artistDetails.value!!.id
                                    }) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Favorite",
                                tint = Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }

    ) { innerPadding ->
        Column {
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) },
                        text = {
                            Text(text = title, color = MaterialTheme.colorScheme.onPrimary)
                        },
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = title,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> DetailsScreen(sharedViewModel = sharedViewModel)
                1 -> ArtworksScreen()
                2 -> SimilarArtistsScreen()
            }
        }
    }
}
