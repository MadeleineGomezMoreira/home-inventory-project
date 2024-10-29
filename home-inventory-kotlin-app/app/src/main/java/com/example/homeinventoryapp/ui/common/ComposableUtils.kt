package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingProgressComponent(showComponent: Boolean, modifier: Modifier = Modifier) {
    if (showComponent) {
        CircularProgressIndicator(
            modifier = modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun ShowSnackbarMessage(
    message: String? = null,
    snackbarHostState: SnackbarHostState,
    onMessageShown: () -> Unit) {
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Long,
            )
            onMessageShown()
        }
    }
}