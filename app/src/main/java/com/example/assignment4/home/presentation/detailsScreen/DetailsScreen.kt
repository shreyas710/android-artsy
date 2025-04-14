package com.example.assignment4.home.presentation.detailsScreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailsScreen() {
    println("Inside details screen")
    Text("Details Screen", color = MaterialTheme.colorScheme.onPrimary)
}