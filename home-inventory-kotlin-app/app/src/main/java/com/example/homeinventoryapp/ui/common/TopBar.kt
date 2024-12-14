package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()  // Fill only the width
            .height(85.dp)   // Set a fixed height for the top bar
    ) {
        // Background image with blur effect
        DefaultImage(
            modifier = Modifier
                .fillMaxSize()
//                .graphicsLayer {
//                    this.alpha = 0.1f  // Apply some transparency to the background image
//                }
                .blur(15.dp)  // Apply blur
        )
    }
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title.uppercase(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    )
}

@Preview
@Composable
fun TopAppBarPreview() {
    TopBar(title = "SCREEN TITLE")
}