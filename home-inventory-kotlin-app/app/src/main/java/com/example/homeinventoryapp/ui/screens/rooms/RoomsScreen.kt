package com.example.homeinventoryapp.ui.screens.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.domain.model.Room
import com.example.homeinventoryapp.ui.common.ClickableBigCard
import com.example.homeinventoryapp.ui.common.CreateItemDialog
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.ListBigCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession

@Composable
fun RoomsScreen(
    viewModel: RoomsViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onRoomClicked: (Any?) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    val homeId = UserSession.homeId

    LaunchedEffect(homeId) {
        homeId?.let { viewModel.handleEvent(RoomsContract.RoomsEvent.GetRooms(homeId)) }
    }

    if (uiState.roomId != null) {
        val roomId = uiState.roomId
        viewModel.handleEvent(RoomsContract.RoomsEvent.ClearRoom)
        onRoomClicked(roomId)
    }

    RoomsContent(
        rooms = uiState.rooms,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(RoomsContract.RoomsEvent.ErrorDisplayed)
        },
        onRoomClicked = { room ->
            viewModel.handleEvent(RoomsContract.RoomsEvent.RoomClicked(room as Room))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.handleEvent(RoomsContract.RoomsEvent.ShowDialogue)
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Room")
            }
        }
    )

    if (uiState.showCreateDialogue) {
        CreateItemDialog(
            onDismiss = { viewModel.handleEvent(RoomsContract.RoomsEvent.ClearDialogue) },
            onItemCreate = { homeName ->
                homeId?.let {
                    viewModel.handleEvent(
                        RoomsContract.RoomsEvent.CreateRoom(
                            homeId,
                            homeName
                        )
                    )
                }
                viewModel.handleEvent(RoomsContract.RoomsEvent.ClearDialogue)
            },
            itemToCreateWord = "Room"
        )
    }

}

@Composable
fun RoomsContent(
    rooms: List<Room>? = emptyList(),
    error: String?,
    isLoading: Boolean = false,
    onRoomClicked: (Any?) -> Unit = {},
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
            // Background image with blur effect
            DefaultImage(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.alpha = 0.6f  // Apply some transparency to the background image
                    }
                    .blur(15.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (rooms?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                RoomListBigCard(
                    rooms = rooms,
                    onRoomClicked = onRoomClicked
                )
            }
            if (rooms.isNullOrEmpty()) {
                CustomTextBold(
                    text = "No rooms found.",
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
fun RoomListBigCard(
    rooms: List<Room>,
    onRoomClicked: (Room) -> Unit
) {
    ListBigCard(
        items = rooms,
        onItemClicked = onRoomClicked
    ) { room ->
        ClickableBigCard(
            item = room,
            onItemClicked = onRoomClicked,
            iconContent = { RoomIcon() },
            imageContent = { RoomImage() },
            textContent = { it.name }
        )
    }
}

@Composable
fun RoomIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.DoorFront,
    contentDescription: String? = null,
    tint: Color = Color.White,
    size: Dp = 40.dp
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.size(size)
    )
}

@Composable
fun RoomImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = "Default background image"
) {
    Image(
        painter = painterResource(id = R.drawable.van_gogh_room),
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}