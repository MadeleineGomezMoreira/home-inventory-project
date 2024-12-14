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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession
import com.example.homeinventoryapp.utils.Constants

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
    username: String = Constants.EMPTY_STRING,
    password: String = Constants.EMPTY_STRING,
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

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultImage(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.alpha = 0.5f
                    }
                    .blur(10.dp)
            )
        }

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
                label = stringResource(R.string.username),
            )

            CustomTextField(
                value = password,
                onValueChange = { password -> changedPassword(password) },
                label = stringResource(R.string.password),
                isPassword = true
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
            .wrapContentWidth()
            .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.width(3.dp))
        Button(
            onClick = { onClickAction() },
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(ButtonDefaults.buttonColors().disabledContainerColor),
            content = {
                Text(
                    text = buttonText,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    fontSize = 20.sp
                )
            }
        )
        Spacer(modifier = Modifier.width(3.dp))
    }
}


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    fontSize: Int = 25,
    labelFontSize: Int = 18,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, fontSize = labelFontSize.sp) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) Constants.HIDE_PASSWORD_DESCRIPTION else Constants.SHOW_PASSWORD_DESCRIPTION
                    )
                }
            }
        },
        textStyle = TextStyle(fontSize = fontSize.sp)
    )
}
