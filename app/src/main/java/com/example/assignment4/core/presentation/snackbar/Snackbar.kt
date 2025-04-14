package com.example.assignment4.core.presentation.snackbar

import androidx.compose.runtime.Composable

@Composable
fun Snackbar(
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Snackbar(
        message = message,
        actionLabel = actionLabel,
        onAction = onAction,
    )
}
