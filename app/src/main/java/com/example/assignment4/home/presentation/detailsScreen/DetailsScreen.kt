package com.example.assignment4.home.presentation.detailsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailsScreen(sharedViewModel: SharedViewModel) {
    val artist = sharedViewModel.selectedArtist.value!!
    val scrollState = rememberScrollState()


    LaunchedEffect(artist.links.self.href) {
        val response = RetrofitInstance.api.getArtist(artist.links.self.href.split("/").last())
        sharedViewModel.artistDetails.value = response
    }

    val details = sharedViewModel.artistDetails.value

    if (details == null) {
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
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp, top = 0.dp, bottom = 30.dp)
        ) {
            Text(
                text = details.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 25.sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "${details.nationality}, ${details.birthday} - ${details.deathday}",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = details.biography,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
            )
        }
    }
}