package com.example.assignment4.home.presentation.home

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.assignment4.core.data.api.RetrofitInstance
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.assignment4.auth.data.models.Favorites
import com.example.assignment4.auth.data.models.LoginResponse
import com.example.assignment4.core.data.api.LoginDataStoreManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current

    val loginResponse by produceState<LoginResponse?>(initialValue = null, key1 = context) {
        value = LoginDataStoreManager.getLoginResponse(context)
    }
    val coroutineScope = rememberCoroutineScope()

    val (loading, setLoading) = remember { mutableStateOf(false) }

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val formattedDate = today.format(formatter)

    LaunchedEffect(Unit) {
        setLoading(true)
        sharedViewModel.userFavorite.value = emptyList<Favorites>()
        try {
            RetrofitInstance.artsyApi.getXappToken()
            sharedViewModel.token.value = RetrofitInstance.cookieJar.getCookieValue("token")

            sharedViewModel.user.value = loginResponse

            sharedViewModel.user.value!!.favorites.forEach {
                val response = RetrofitInstance.artsyApi.getArtist(it.id)
                sharedViewModel.userFavorite.value = sharedViewModel.userFavorite.value + Favorites(
                    artist = response,
                    createdAt = it.createdAt
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            setLoading(false)
        }
    }

    fun formatTimeAgo(createdDate: Date): String {
        val seconds = ((Date().time - createdDate.time) / 1000).toInt()

        if (seconds < 60) {
            return "$seconds second${if (seconds != 1) "s" else ""} ago"
        }

        val minutes = seconds / 60
        if (minutes < 60) {
            return "$minutes minute${if (minutes != 1) "s" else ""} ago"
        }

        val hours = minutes / 60
        if (hours < 24) {
            return "$hours hour${if (hours != 1) "s" else ""} ago"
        }

        val days = hours / 24
        return "$days day${if (days != 1) "s" else ""} ago"
    }

    var expanded by remember { mutableStateOf(false) }

    suspend fun deleteAccount() {
        try {
            val response =
                RetrofitInstance.userApi.deleteUser("Bearer ${sharedViewModel.user.value!!.token}")
            if (response.isSuccessful) {
                LoginDataStoreManager.clearLoginResponse(context)
                sharedViewModel.user.value = null
            }
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
                        Text(
                            "Artist Search",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
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
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = {
                        if (sharedViewModel.user.value != null) {
                            expanded = !expanded
                        } else {
                            navController.navigate("login")
                        }
                    }) {
                        Icon(
                            painter = if (!sharedViewModel.user.value?.pic.isNullOrEmpty()) {
                                rememberAsyncImagePainter(model = sharedViewModel.user.value!!.pic)
                            } else {
                                rememberVectorPainter(image = Icons.Default.Person)
                            },
                            contentDescription = "Account",
                            modifier = Modifier
                                .size(
                                    if (!sharedViewModel.user.value?.pic.isNullOrEmpty()) {
                                        30.dp
                                    } else {
                                        25.dp
                                    }
                                )
                                .clip(RoundedCornerShape(50.dp)),
                            tint = if (!sharedViewModel.user.value?.pic.isNullOrEmpty()) {
                                Color.Unspecified
                            } else {
                                Color.Black
                            }
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Log out",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 15.sp
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    LoginDataStoreManager.clearLoginResponse(context)
                                    sharedViewModel.user.value = null
                                    expanded = false
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Account", color = Color.Red, fontSize = 15.sp) },
                            onClick = {
                                coroutineScope.launch {
                                    deleteAccount()
                                    expanded = false
                                }
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 30.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        } else {
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
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Favorites",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    if (sharedViewModel.user.value == null) {
                        Button(
                            onClick = { navController.navigate("login") },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text(
                                "Log in to see favorites",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else if (sharedViewModel.user.value!!.favorites.isEmpty()) {
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
                                text = "No favorites",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        sharedViewModel.userFavorite.value.reversed().forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = it.artist.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                    )
                                    Text(
                                        text = "${it.artist.nationality}, ${it.artist.birthday} - ${it.artist.deathday}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formatTimeAgo(it.createdAt),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Go to details",
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    TextButton(
                        onClick = {
                            val url = "https://www.artsy.net/"
                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }, colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            "Powered by Artsy",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

