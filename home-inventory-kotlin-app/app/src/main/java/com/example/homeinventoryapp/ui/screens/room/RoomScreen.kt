package com.example.homeinventoryapp.ui.screens.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chair
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
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.domain.model.Room
import com.example.homeinventoryapp.ui.common.ClickableSmallCard
import com.example.homeinventoryapp.ui.common.CreateItemDialog
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultIcon
import com.example.homeinventoryapp.ui.common.EditItemDialog
import com.example.homeinventoryapp.ui.common.FloatingActionMenuAddEdit
import com.example.homeinventoryapp.ui.common.ListSmallCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession
import timber.log.Timber

@Composable
fun RoomScreen(
    viewModel: RoomViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onFurnitureClicked: (Any?) -> Unit,
    roomId: Int,
) {
    val uiState by viewModel.state.collectAsState()
    val homeId = UserSession.homeId ?: 0

    LaunchedEffect(roomId) {
        viewModel.handleEvent(RoomContract.RoomEvent.GetRoom(roomId))
        viewModel.handleEvent(RoomContract.RoomEvent.GetRoomFurniture(roomId))
    }

    if (uiState.furnitureId != null) {
        val furnId = uiState.furnitureId
        Timber.d("Furniture ID: $furnId")
        viewModel.handleEvent(RoomContract.RoomEvent.ClearFurniture)
        onFurnitureClicked(furnId)
    }

    RoomContent(
        room = uiState.room,
        furniture = uiState.furniture,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(RoomContract.RoomEvent.ErrorDisplayed)
        },
        onFurnitureClicked = { furniture ->
            viewModel.handleEvent(RoomContract.RoomEvent.FurnitureClicked(furniture as Furniture))
        },
        floatingActionButton = {
            FloatingActionMenuAddEdit(
                onAddElementClicked = {
                    viewModel.handleEvent(RoomContract.RoomEvent.ShowCreateDialogue)
                },
                onEditElementClicked = {
                    viewModel.handleEvent(RoomContract.RoomEvent.ShowEditDialogue)
                },
            )
        }
    )
    if (uiState.showCreateDialogue) {
        CreateItemDialog(
            onDismiss = { viewModel.handleEvent(RoomContract.RoomEvent.ClearCreateDialogue) },
            onItemCreate = { furnitureName ->
                viewModel.handleEvent(RoomContract.RoomEvent.CreateFurniture(furnitureName, roomId))
                viewModel.handleEvent(RoomContract.RoomEvent.ClearCreateDialogue)
            },
            itemToCreateWord = "Furniture"
        )
    }
    if (uiState.showEditDialogue) {
        EditItemDialog(
            onDismiss = { viewModel.handleEvent(RoomContract.RoomEvent.ClearEditDialogue) },
            onItemEdit = { roomName ->
                viewModel.handleEvent(RoomContract.RoomEvent.EditRoom(roomName, roomId, homeId))
                viewModel.handleEvent(RoomContract.RoomEvent.ClearEditDialogue)
            },
            itemToEditWord = "Room",

        )
    }

}

@Composable
fun RoomContent(
    room: Room?,
    furniture: List<Furniture>? = emptyList(),
    error: String?,
    isLoading: Boolean = false,
    onFurnitureClicked: (Any?) -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {},
    errorShown: () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topBar,
        bottomBar = bottomNavigationBar,
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextBold(text = room?.name ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            if (furniture?.isNotEmpty() == true) {
                Text(text = "Furniture", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                FurnitureListSmallCard(
                    furniture = furniture,
                    onFurnitureClicked = onFurnitureClicked
                )
            } else {
                Text(text = "No furniture added yet")
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

}

@Composable
fun FurnitureListSmallCard(
    furniture: List<Furniture>,
    onFurnitureClicked: (Furniture) -> Unit,
) {
    ListSmallCard(
        items = furniture,
        onItemClicked = onFurnitureClicked
    ) { furniture ->
        ClickableSmallCard(
            item = furniture,
            onItemClicked = onFurnitureClicked,
            textContent = { it.name },
            iconContent = {
                DefaultIcon(
                    imageVector = Icons.Default.Chair,
                    contentDescription = "User icon",
                    size = 60.dp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        )
    }


}