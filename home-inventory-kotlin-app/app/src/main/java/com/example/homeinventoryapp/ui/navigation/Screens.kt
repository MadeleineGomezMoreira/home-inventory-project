package com.example.homeinventoryapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.homeinventoryapp.utils.Constants

val screensBottomBar = listOf(
    Screens(Constants.HOME_ROUTE, Constants.HOME_BAR_NAME, Icons.Default.Home),
    Screens(Constants.MY_HOMES_ROUTE, Constants.MY_HOMES_BAR_NAME, Icons.Default.HomeWork),
    Screens(Constants.ACCOUNT_ROUTE, Constants.ACCOUNT_BAR_NAME, Icons.Default.AccountCircle),
    Screens(Constants.HOME_SCREEN_ROUTE, Constants.HOME_SCREEN_BAR_NAME, Icons.Default.Handshake),
)

val screensHomeBottomBar = listOf(
    Screens(Constants.MY_HOMES_ROUTE, Constants.MY_HOMES_BAR_NAME, Icons.Default.HomeWork),
    Screens(Constants.ROOMS_ROUTE, Constants.ROOMS_BAR_NAME, Icons.Default.DoorFront),
    Screens(Constants.SEARCH_ITEMS_ROUTE, Constants.SEARCH_ITEMS_BAR_NAME, Icons.Default.Search),
)

val screensLoginRegisterBottomBar = listOf(
    Screens(Constants.LOGIN_ROUTE, Constants.LOGIN_BAR_NAME, Icons.AutoMirrored.Filled.Login),
    Screens(Constants.REGISTER_ROUTE, Constants.REGISTER_BAR_NAME, Icons.Default.AssignmentInd),
)

data class Screens(
    val route: String,
    val name: String,
    val icon: ImageVector,
)
