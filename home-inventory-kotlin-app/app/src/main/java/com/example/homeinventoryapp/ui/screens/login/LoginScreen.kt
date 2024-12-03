package com.example.homeinventoryapp.ui.screens.login

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.ui.common.di.UserSession

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit = {},
    onLoginSuccess: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val uiState by viewModel.state.collectAsState()

    if (uiState.userId != null) {
        UserSession.userId = uiState.userId
        viewModel.handleEvent(LoginContract.LoginEvent.ClearUserId)
        onLoginSuccess()
    }

    LoginContent(
        onLogin = {
            viewModel.handleEvent(LoginContract.LoginEvent.Login)
        },
        username = uiState.username,
        password = uiState.password,
        errorShown = {
            viewModel.handleEvent(LoginContract.LoginEvent.ErrorDisplayed)
        },
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar
    )

}

@Composable
fun LoginContent(
    topBar: @Composable () -> Unit = {},
    onLogin: () -> Unit,
    username: String = "",
    password: String = "",
    errorShown: () -> Unit = {},
    isLoading: Boolean = false,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topBar,
    ) { innerPadding ->

    }
}