package com.example.assignment4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assignment4.home.presentation.home.HomeScreen
import com.example.assignment4.home.presentation.search.SearchContent
import com.example.assignment4.core.presentation.viewModel.SharedViewModel
import com.example.assignment4.home.presentation.artistDetails.ArtistDetails
import com.example.assignment4.ui.theme.Assignment4Theme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment4Theme {
                var navController = rememberNavController()
                var sharedViewModel: SharedViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController = navController, sharedViewModel = sharedViewModel)
                    }
                    composable("search") {
                        SearchContent(
                            navController = navController,
                            sharedViewModel = sharedViewModel
                        )
                    }
                    composable("artistDetails") {
                        ArtistDetails(
                            navController = navController,
                            sharedViewModel = sharedViewModel
                        )
                    }
                }
            }
        }
    }
}