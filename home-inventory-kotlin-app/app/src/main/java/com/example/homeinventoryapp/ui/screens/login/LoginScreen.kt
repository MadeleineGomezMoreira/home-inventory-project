package com.example.homeinventoryapp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
        changedPassword = {
            viewModel.handleEvent(LoginContract.LoginEvent.PasswordChanged(it))
        },
        changedUsername = {
            viewModel.handleEvent(LoginContract.LoginEvent.UsernameChanged(it))
        },
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        error = uiState.error
    )

}

@Composable
fun LoginContent(
    topBar: @Composable () -> Unit = {},
    onLogin: () -> Unit,
    username: String = "",
    password: String = "",
    error: String?,
    errorShown: () -> Unit = {},
    isLoading: Boolean = false,
    changedUsername: (String) -> Unit = {},
    changedPassword: (String) -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {},
) {
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
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
            )
            LoginRegisterButtonRow(
                onClickAction = onLogin,
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


@Composable
fun LoginRegisterButtonRow(
    onClickAction: () -> Unit = {},
    buttonText: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.width(3.dp))
        Button(
            onClick = { onClickAction() },
            modifier = Modifier
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            )
        ) {
            Text(text = buttonText)
        }
        Spacer(modifier = Modifier.width(3.dp))
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    )
}