package com.example.homeinventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homeinventoryapp.ui.common.TopBar
import com.example.homeinventoryapp.ui.screens.HomeScreen
import com.example.homeinventoryapp.utils.Constants

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Constants.HOME_SCREEN_ROUTE,
    ) {
        composable(
            route = Constants.HOME_SCREEN_ROUTE
        ) {
            HomeScreen(
                topBar = {
                    TopBar(
                        title = Constants.HOME_SCREEN_BAR_NAME
                    )
                })
        }
    }
}