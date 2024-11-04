package com.example.homeinventoryapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.User
import com.example.homeinventoryapp.ui.common.ClickableSmallCard
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultIcon
import com.example.homeinventoryapp.ui.common.ListSmallCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onUserClicked: (Any?) -> Unit,
    homeId: Int,
) {
    val uiState by viewModel.state.collectAsState()

    //TODO: if owner id matches userId, show delete + edit buttons

    LaunchedEffect(homeId) {
        viewModel.handleEvent(HomeContract.HomeEvent.GetHome(homeId))
        viewModel.handleEvent(HomeContract.HomeEvent.GetHomeUsers(homeId))
    }

    if (uiState.userId != null) {
        val userId = uiState.userId
        viewModel.handleEvent(HomeContract.HomeEvent.ClearUser)
        onUserClicked(userId)
    }

    HomeContent(
        owner = uiState.owner,
        members = uiState.members,
        home = uiState.home,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(HomeContract.HomeEvent.ErrorDisplayed)
        },
        onUserClicked = { user ->
            viewModel.handleEvent(HomeContract.HomeEvent.UserClicked(user as User))
        },

        )
}

@Composable
fun HomeContent(
    owner: User?,
    members: List<User>? = emptyList(),
    home: Home?,
    error: String?,
    isLoading: Boolean = false,
    onUserClicked: (Any?) -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {},
    errorShown: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topBar,
        bottomBar = bottomNavigationBar,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextBold(text = home?.name ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            if (owner != null) {
                Text(text = "House Owner", style = MaterialTheme.typography.titleLarge)
                ClickableSmallCard(
                    item = owner,
                    onItemClicked = onUserClicked,
                    textContent = { it.username },
                    iconContent = {
                        DefaultIcon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User icon",
                            size = 60.dp,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                )
            }
            if (members?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "House Members", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                UsersListSmallCard(
                    users = members,
                    onUserClicked = onUserClicked
                )
            }
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
fun UsersListSmallCard(
    users: List<User>,
    onUserClicked: (User) -> Unit
) {
    ListSmallCard(
        items = users,
        onItemClicked = onUserClicked
    ) { user ->
        ClickableSmallCard(
            item = user,
            onItemClicked = onUserClicked,
            textContent = { it.username },
            iconContent = {
                DefaultIcon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User icon",
                    size = 60.dp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        )
    }
}