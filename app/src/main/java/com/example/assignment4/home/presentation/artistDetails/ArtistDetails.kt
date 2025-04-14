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
import androidx.navigation.NavController
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.presentation.artworksScreen.ArtworksScreen
import com.example.assignment4.home.presentation.detailsScreen.DetailsScreen
import com.example.assignment4.home.presentation.similarArtistsScreen.SimilarArtistsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetails(navController: NavController, sharedViewModel: SharedViewModel) {
    val tabs = remember { mutableListOf("Details", "Artworks") }
    val icons = remember { mutableListOf(Icons.Default.Info, Icons.Default.AccountBox) }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val userLoggedIn by remember { mutableStateOf(false) }

    if (userLoggedIn && !tabs.contains("Similar Artists")) {
        tabs.add("Similar Artists")
    }

    if (userLoggedIn && !icons.contains(Icons.Default.PersonSearch)) {
        icons.add(Icons.Default.PersonSearch)
    }

    fun onTabSelected(index: Int) {
        selectedTabIndex = index
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
                            sharedViewModel.selectedArtist.value!!.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
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
