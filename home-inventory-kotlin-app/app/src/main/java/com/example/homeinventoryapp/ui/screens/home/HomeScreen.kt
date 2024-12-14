package com.example.homeinventoryapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.User
import com.example.homeinventoryapp.ui.common.ClickableSmallCard
import com.example.homeinventoryapp.ui.common.DefaultIcon
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.EditItemDialog
import com.example.homeinventoryapp.ui.common.FloatingActionMenuEditDeleteAdd
import com.example.homeinventoryapp.ui.common.ListSmallCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.SendInvitationDialog
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession
import com.example.homeinventoryapp.ui.screens.item.ResizableText
import com.example.homeinventoryapp.utils.Constants

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    onUserClicked: (Any?) -> Unit,
    onHomeWasDeleted: () -> Unit,
    homeId: Int,
) {
    val uiState by viewModel.state.collectAsState()
    val userId = UserSession.userId ?: 0

    LaunchedEffect(homeId) {
        viewModel.handleEvent(HomeContract.HomeEvent.GetHome(homeId))
        viewModel.handleEvent(HomeContract.HomeEvent.GetHomeUsers(homeId))
        viewModel.handleEvent(HomeContract.HomeEvent.GetUserOwner(homeId, UserSession.userId ?: 0))
        UserSession.homeId = homeId
    }

    if (uiState.userId != null) {
        val userId = uiState.userId
        viewModel.handleEvent(HomeContract.HomeEvent.ClearUser)
        onUserClicked(userId)
    }
    if (uiState.homeWasDeleted) {
        viewModel.handleEvent(HomeContract.HomeEvent.ClearHomeWasDeleted)
        onHomeWasDeleted()
    }

    HomeContent(
        owner = uiState.owner,
        members = uiState.members,
        home = uiState.home,
        error = uiState.error,
        isLoading = uiState.isLoading,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(HomeContract.HomeEvent.ErrorDisplayed)
        },
        onUserClicked = { user ->
            viewModel.handleEvent(HomeContract.HomeEvent.UserClicked(user as User))
        },
        floatingActionButton = {
            if (uiState.isUserOwner == true) {
                FloatingActionMenuEditDeleteAdd(
                    onDeleteElementClicked = {
                        viewModel.handleEvent(HomeContract.HomeEvent.DeleteHome(homeId))
                    },
                    onEditElementClicked = {
                        viewModel.handleEvent(HomeContract.HomeEvent.ShowEditDialogue)
                    },
                    onAddHomeMember = {
                        viewModel.handleEvent(HomeContract.HomeEvent.ShowInvitationDialogue)
                    }
                )
            }
        }
    )

    if (uiState.showInvitationDialogue) {
        SendInvitationDialog(
            onDismiss = {
                viewModel.handleEvent(HomeContract.HomeEvent.ClearInvitationDialogue)
            },
            onSendInvitation = { username ->
                viewModel.handleEvent(
                    HomeContract.HomeEvent.SendInvitation(
                        userId,
                        username,
                        homeId
                    )
                )
            }
        )
    }

    if (uiState.showEditDialogue) {
        EditItemDialog(
            onDismiss = { viewModel.handleEvent(HomeContract.HomeEvent.ClearEditDialogue) },
            onItemEdit = { homeName ->
                viewModel.handleEvent(HomeContract.HomeEvent.EditHome(homeName, homeId))
            },
            itemToEditWord = Constants.HOME
        )
    }
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
    floatingActionButton: @Composable () -> Unit = {},
    errorShown: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topBar,
        bottomBar = bottomNavigationBar,
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultImage(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.alpha = 0.4f
                    }
                    .blur(15.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            DefaultImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .blur(10.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                ResizableText(home?.name?.uppercase() ?: stringResource(R.string.name_not_found))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (owner != null) {
                Text(
                    text = stringResource(R.string.home_owner),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
                ClickableSmallCard(
                    item = owner,
                    onItemClicked = onUserClicked,
                    textContent = { it.username },
                    iconContent = {
                        DefaultIcon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = Constants.USER_ICON_DESCRIPTION,
                            size = 60.dp,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                )
            }
            if (members?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.home_members),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                UsersListSmallCard(
                    users = members,
                    onUserClicked = {
                        /* no OnClick needed */
                    }
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
                    contentDescription = Constants.USER_ICON_DESCRIPTION,
                    size = 60.dp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        )
    }
}