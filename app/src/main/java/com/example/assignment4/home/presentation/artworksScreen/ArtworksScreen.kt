package com.example.assignment4.home.presentation.artworksScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.presentation.artworkCard.ArtworkCard

@Composable
fun ArtworksScreen(sharedViewModel: SharedViewModel) {
    val (loader, setLoader) = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        setLoader(true)
        try {
            val response =
                RetrofitInstance.artsyApi.getArtistArtworks(id = sharedViewModel.artistDetails.value!!.id)
            if (response.isSuccessful) {
                sharedViewModel.artistArtworks.value = response.body()!!
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
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(top = 50.dp)
            )
        }
    } else if (sharedViewModel.artistArtworks.value.isEmpty()) {
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
                text = "No artworks",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                color = Color.Black
            )
        }
    } else {
        LazyColumn {
            items(sharedViewModel.artistArtworks.value.size) { index ->
                val artwork = sharedViewModel.artistArtworks.value[index]
                ArtworkCard(artwork, sharedViewModel)
            }
        }
    }
}