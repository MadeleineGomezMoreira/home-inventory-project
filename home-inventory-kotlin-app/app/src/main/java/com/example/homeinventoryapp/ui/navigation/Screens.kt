package com.example.homeinventoryapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.homeinventoryapp.utils.Constants

val screensBottomBar = listOf(
    Screens(Constants.HOME_ROUTE, Constants.HOME_BAR_NAME, Icons.Default.Home),
    Screens(Constants.HOMES_ROUTE, Constants.HOMES_BAR_NAME, Icons.Default.HomeWork),
    Screens(Constants.ACCOUNT_ROUTE, Constants.ACCOUNT_BAR_NAME, Icons.Default.AccountCircle),
    Screens(Constants.HOME_SCREEN_ROUTE, Constants.HOME_SCREEN_BAR_NAME, Icons.Default.Handshake),
)

data class Screens(
    val route: String,
    val name: String,
    val icon: ImageVector,
)
