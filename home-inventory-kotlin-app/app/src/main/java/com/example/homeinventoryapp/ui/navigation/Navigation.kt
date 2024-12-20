package com.example.homeinventoryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homeinventoryapp.ui.common.BottomBar
import com.example.homeinventoryapp.ui.common.TopBar
import com.example.homeinventoryapp.ui.screens.account.AccountScreen
import com.example.homeinventoryapp.ui.screens.compartment.CompartmentScreen
import com.example.homeinventoryapp.ui.screens.furniture.FurnitureScreen
import com.example.homeinventoryapp.ui.screens.home.HomeScreen
import com.example.homeinventoryapp.ui.screens.item.ItemScreen
import com.example.homeinventoryapp.ui.screens.login.LoginScreen
import com.example.homeinventoryapp.ui.screens.myhomes.MyHomesScreen
import com.example.homeinventoryapp.ui.screens.register.RegisterScreen
import com.example.homeinventoryapp.ui.screens.room.RoomScreen
import com.example.homeinventoryapp.ui.screens.rooms.RoomsScreen
import com.example.homeinventoryapp.ui.screens.search.SearchScreen
import com.example.homeinventoryapp.utils.Constants

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Constants.LOGIN_ROUTE,
    ) {
        composable(
            route = Constants.REGISTER_ROUTE
        ) {
            RegisterScreen(
                topBar = {
                    TopBar(
                        title = Constants.REGISTER_BAR_NAME
                    )
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screensLoginRegisterBottomBar,
                    )
                },
                onRegisterSuccess = { navController.navigate(Constants.LOGIN_ROUTE) }
            )
        }
        composable(
            route = Constants.LOGIN_ROUTE
        ) {
            LoginScreen(
                topBar = {
                    TopBar(
                        title = Constants.LOGIN_BAR_NAME
                    )
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screensLoginRegisterBottomBar,
                    )
                },
                onLoginSuccess = {
                    navController.navigate(Constants.MY_HOMES_ROUTE)
                }
            )
        }
        composable(
            route = Constants.ACCOUNT_ROUTE,
        ){
            AccountScreen(
                topBar = {
                    TopBar(
                        title = Constants.ACCOUNT_BAR_NAME
                    )
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screensBottomBar,
                    )
                },
                onAccountDeleted = {
                    navController.navigate(Constants.LOGIN_ROUTE)
                }
            )
        }
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
                    bottomNavigationBar = {
                        BottomBar(
                            navController = navController,
                            screens = screensHomeBottomBar,
                        )
                    },
                    homeId = retrievedHomeId.toInt(),
                    onUserClicked = {
                        navController.navigate(Constants.ACCOUNT_ROUTE)
                    },
                    onHomeWasDeleted = {
                        navController.navigate(Constants.MY_HOMES_ROUTE)
                    }
                    )
            }
        }
        composable(
            route = Constants.SEARCH_ITEMS_ROUTE
        ) {
            SearchScreen(
                topBar = {
                    TopBar(
                        title = Constants.SEARCH_ITEMS_BAR_NAME
                    )
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screensHomeBottomBar,
                    )
                },
                onItemClicked = { itemId ->
                    navController.navigate("item_screen/$itemId")
                },
            )
        }
        composable(
            route = Constants.ROOM_ROUTE,
            arguments = listOf(navArgument(Constants.ROOM_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val retrievedRoomId = backStackEntry.arguments?.getString(Constants.ROOM_ID)
            if (retrievedRoomId != null) {
                RoomScreen(
                    topBar = {
                        TopBar(
                            title = Constants.ROOM_BAR_NAME
                        )
                    },
                    bottomNavigationBar = {
                        BottomBar(
                            navController = navController,
                            screens = screensHomeBottomBar,
                        )
                    },
                    onFurnitureClicked = { furnId ->
                        navController.navigate("furniture_screen/$furnId")
                    },
                    roomId = retrievedRoomId.toInt(),
                )
            }
        }
        composable(
            route = Constants.ITEM_ROUTE,
            arguments = listOf(navArgument(Constants.ITEM_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val retrievedItemId = backStackEntry.arguments?.getString(Constants.ITEM_ID)
            if (retrievedItemId != null) {
                ItemScreen(
                    bottomNavigationBar = {
                        BottomBar(
                            navController = navController,
                            screens = screensHomeBottomBar,
                        )
                    },
                    itemId = retrievedItemId.toInt(),
                )
            }
        }
        composable(
            route = Constants.COMPARTMENT_ROUTE,
            arguments = listOf(navArgument(Constants.COMP_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val retrievedCompId = backStackEntry.arguments?.getString(Constants.COMP_ID)
            if (retrievedCompId != null) {
                CompartmentScreen(
                    bottomNavigationBar = {
                        BottomBar(
                            navController = navController,
                            screens = screensHomeBottomBar,
                        )
                    },
                    topBar = {
                        TopBar(
                            title = Constants.COMPARTMENT_BAR_NAME
                        )
                    },
                    onItemClicked = { itemId ->
                        navController.navigate("item_screen/$itemId")
                    },
                    compId = retrievedCompId.toInt(),
                )
            }
        }
        composable(
            route = Constants.FURNITURE_ROUTE,
            arguments = listOf(navArgument(Constants.FURN_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val retrievedFurnId = backStackEntry.arguments?.getString(Constants.FURN_ID)
            if (retrievedFurnId != null) {
                FurnitureScreen(
                    bottomNavigationBar = {
                        BottomBar(
                            navController = navController,
                            screens = screensHomeBottomBar,
                        )
                    },
                    topBar = {
                        TopBar(
                            title = Constants.FURNITURE_BAR_NAME
                        )
                    },
                    onCompartmentClicked = { compId ->
                        navController.navigate("compartment_screen/$compId")
                    },
                    furnId = retrievedFurnId.toInt(),
                )
            }
        }
        composable(
            route = Constants.ROOMS_ROUTE,
        ) {
            RoomsScreen(
                topBar = {
                    TopBar(
                        title = Constants.ROOMS_BAR_NAME
                    )
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController,
                        screens = screensHomeBottomBar,
                    )
                },
                onRoomClicked = { roomId ->
                    navController.navigate("room_screen/$roomId")
                },

                )
        }
    }
}