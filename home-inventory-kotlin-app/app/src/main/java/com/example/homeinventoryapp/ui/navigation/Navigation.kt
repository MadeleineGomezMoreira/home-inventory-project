package com.example.homeinventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homeinventoryapp.ui.common.BottomBar
import com.example.homeinventoryapp.ui.common.TopBar
import com.example.homeinventoryapp.ui.screens.home.HomeScreen
import com.example.homeinventoryapp.ui.screens.myhomes.MyHomesScreen
import com.example.homeinventoryapp.utils.Constants

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        //HERE SHOULD GO LOGIN-SCREEN ROUTE
        startDestination = Constants.MY_HOMES_ROUTE,
    ) {
        composable(
            route = Constants.MY_HOMES_ROUTE
        ) {
            MyHomesScreen(
                topBar = {
                    TopBar(
                        title = Constants.MY_HOMES_BAR_NAME
                    )
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screensBottomBar,
                    )
                },
                onHomeClicked = { homeId ->
                    navController.navigate("home_screen/$homeId")
                }
            )
        }
        composable(
            route = Constants.HOME_ROUTE,
            arguments = listOf(navArgument(Constants.HOME_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val retrievedHomeId = backStackEntry.arguments?.getString(Constants.HOME_ID)
            if (retrievedHomeId != null) {
                HomeScreen(
                    topBar = {
                        TopBar(
                            title = Constants.HOME_BAR_NAME
                        )
                    },
                    bottomNavigationBar = {
                        BottomBar(
                            navController = navController,
                            screens = screensHomeBottomBar,
                        )
                    },
                    homeId = retrievedHomeId.toInt(),
                    onUserClicked = { userId ->
                        //navController.navigate("user_profile_screen/$userId")
                    },

                )
            }
        }
    }
}