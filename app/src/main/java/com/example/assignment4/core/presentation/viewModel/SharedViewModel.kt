package com.example.assignment4.core.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var token = mutableStateOf<String?>(null)
}