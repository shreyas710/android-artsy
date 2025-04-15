package com.example.assignment4.home.presentation.similarArtistsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.presentation.artistCard.ArtistCard

@Composable
fun SimilarArtistsScreen(
    sharedViewModel: SharedViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val currArtist = sharedViewModel.artistDetails.value

    val (loader, setLoader) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        setLoader(true)
        try {
            val response = RetrofitInstance.artsyApi.getSimilarArtists(currArtist!!.id)
            if (response.isSuccessful) {
                sharedViewModel.similarArtists.value = response.body()!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            setLoader(false)
        }
    }

    if (loader) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(top = 50.dp)
            )
        }
    } else if (sharedViewModel.similarArtists.value.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(30))
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No similar artists",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(sharedViewModel.similarArtists.value.size) { artistId ->
                    ArtistCard(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        artist = sharedViewModel.similarArtists.value[artistId],
                        snackbarHostState = snackbarHostState,
                        similarArtists = true
                    )
                }
            }
        }
    }
}