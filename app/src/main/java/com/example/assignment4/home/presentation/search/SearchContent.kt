package com.example.assignment4.home.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.presentation.artistCard.ArtistCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(navController: NavController, sharedViewModel: SharedViewModel) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length >= 3) {
            try {
                val response = RetrofitInstance.api.getArtists(searchQuery)
                sharedViewModel.artists.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier.weight(5f)
                                ) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search Artists...",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.Black
                                        )
                                    } else {
                                        Text(
                                            searchQuery,
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(top = 10.dp),
                                            color = Color.Black
                                        )
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = {
                                                searchQuery = ""
                                                sharedViewModel.artists.value = emptyList()
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = "Clear",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        },
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(sharedViewModel.artists.value.size) { artistId ->
                    ArtistCard(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        artist = sharedViewModel.artists.value[artistId]
                    )
                }
            }
        }
    }
}
