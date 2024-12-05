package com.example.homeinventoryapp.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.screens.login.CustomTextField
import com.example.homeinventoryapp.ui.screens.login.LoginRegisterButtonRow

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {},
    onRegisterSuccess: () -> Unit,
){
    val uiState by viewModel.state.collectAsState()

    if(uiState.isRegisterSuccessful){
        viewModel.handleEvent(RegisterContract.RegisterEvent.ClearRegisterSuccessful)
        onRegisterSuccess()
    }

    RegisterContent(
        username = uiState.username,
        password = uiState.password,
        email = uiState.email,
        error = uiState.error,
        isLoading = uiState.isLoading,
        errorShown = {
            viewModel.handleEvent(RegisterContract.RegisterEvent.ErrorDisplayed)
        },
        changedUsername = {
            viewModel.handleEvent(RegisterContract.RegisterEvent.UsernameChanged(it))
        },
        changedPassword = {
            viewModel.handleEvent(RegisterContract.RegisterEvent.PasswordChanged(it))
        },
        changedEmail = {
            viewModel.handleEvent(RegisterContract.RegisterEvent.EmailChanged(it))
        },
        bottomNavigationBar = bottomNavigationBar,
        topBar = topBar,
        onRegister = {
            viewModel.handleEvent(RegisterContract.RegisterEvent.Register)
        }
    )

}

@Composable
fun RegisterContent(
    username: String = "",
    password: String = "",
    email: String = "",
    error: String?,
    isLoading: Boolean = false,
    onRegister: () -> Unit = {},
    errorShown: () -> Unit = {},
    changedUsername: (String) -> Unit = {},
    changedPassword: (String) -> Unit = {},
    changedEmail: (String) -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
){
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar,
        topBar = topBar,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            CustomTextField(
                value = username,
                onValueChange = { username -> changedUsername(username) },
                label = stringResource(R.string.username)
            )

            CustomTextField(
                value = password,
                onValueChange = { password -> changedPassword(password) },
                label = stringResource(R.string.password)
            )
            CustomTextField(
                value = email,
                onValueChange = { email -> changedEmail(email) },
                label = stringResource(R.string.email)
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
            )
            LoginRegisterButtonRow(
                onClickAction = onRegister,
                buttonText = stringResource(id = R.string.login_upper),
            )
        }
        ShowSnackbarMessage(message = error, snackbarHostState = snackbarHostState) {
            errorShown()
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingProgressComponent(showComponent = true)
            }
        }
    }
}