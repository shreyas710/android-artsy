package com.example.assignment4.home.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.navigation.NavController
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            RetrofitInstance.api.getXappToken()
            sharedViewModel.token.value = RetrofitInstance.cookieJar.getCookieValue("token")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Artist Search", style = MaterialTheme.typography.titleLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    }
                    IconButton(onClick = { /* Account action */ }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Account",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "01 April 2025",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(red = 236, green = 236, blue = 243)) // light gray
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Favorites",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = { /* TODO: Log in */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Log in to see favorites", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(25.dp))
                TextButton(
                    onClick = {
                        val url = "https://www.artsy.net/"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }, colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        "Powered by Artsy",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                    )
                }
            }
        }

    }
}

