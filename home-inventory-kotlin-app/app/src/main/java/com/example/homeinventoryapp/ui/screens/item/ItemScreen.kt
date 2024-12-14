package com.example.homeinventoryapp.ui.screens.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.EditItemAndTagsDialog
import com.example.homeinventoryapp.ui.common.FloatingActionMenuEditMove
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.MoveItemDialog
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession
import timber.log.Timber

@Composable
fun ItemScreen(
    viewModel: ItemViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    itemId: Int,
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.handleEvent(ItemContract.ItemEvent.GetItem(itemId))
        viewModel.handleEvent(ItemContract.ItemEvent.GetItemRoute(itemId))
    }

    if (uiState.itemMoved) {
        viewModel.handleEvent(ItemContract.ItemEvent.ClearItemMoved)
        viewModel.handleEvent(ItemContract.ItemEvent.GetItemRoute(itemId))
        Timber.d("Item moved: ${uiState.item}")
    }

    if (uiState.showEditDialogue) {
        EditItemAndTagsDialog(item = uiState.item,
            onDismiss = { viewModel.handleEvent(ItemContract.ItemEvent.ClearDialogs) },
            onItemEdit = { viewModel.handleEvent(ItemContract.ItemEvent.UpdateItem(it)) })
    }

    if (uiState.showMoveDialogue) {
        viewModel.handleEvent(ItemContract.ItemEvent.GetRooms(UserSession.homeId ?: 0))
        MoveItemDialog(
            onDismiss = {
                viewModel.handleEvent(ItemContract.ItemEvent.ClearDialogs)
                viewModel.handleEvent(ItemContract.ItemEvent.ClearDialogData)
            },
            onMoveItem = { compartmentId ->
                viewModel.handleEvent(ItemContract.ItemEvent.MoveItem(itemId, compartmentId))
                viewModel.handleEvent(ItemContract.ItemEvent.ClearDialogs)
                viewModel.handleEvent(ItemContract.ItemEvent.ClearDialogData)
            },
            onSelectedRoom = { roomId ->
                viewModel.handleEvent(ItemContract.ItemEvent.GetFurniture(roomId))
            },
            onSelectedFurniture = { furnitureId ->
                viewModel.handleEvent(ItemContract.ItemEvent.GetCompartments(furnitureId))
            },
            rooms = uiState.rooms ?: emptyList(),
            furniture = uiState.furniture ?: emptyList(),
            compartments = uiState.compartments ?: emptyList()
        )
    }

    if (uiState.itemMoved) {
        viewModel.handleEvent(ItemContract.ItemEvent.ClearItemMoved)
        viewModel.handleEvent(ItemContract.ItemEvent.GetItem(itemId))
    }

    ItemContent(
        item = uiState.item,
        itemRoute = uiState.itemRoute,
        error = uiState.error,
        isLoading = uiState.isLoading,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(ItemContract.ItemEvent.ErrorDisplayed)
        },
        floatingActionButton = {
            FloatingActionMenuEditMove(
                onMoveElementClicked = {
                    viewModel.handleEvent(ItemContract.ItemEvent.ShowMoveDialogue)
                },
                onEditElementClicked = {
                    viewModel.handleEvent(ItemContract.ItemEvent.ShowEditDialogue)
                }
            )
        },
        isDialogBeingShown = uiState.showEditDialogue || uiState.showMoveDialogue
    )

}

@Composable
fun ItemContent(
    item: ItemDetail?,
    itemRoute: String?,
    error: String?,
    isLoading: Boolean = false,
    bottomNavigationBar: @Composable () -> Unit = {},
    errorShown: () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    isDialogBeingShown: Boolean = false,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (!isDialogBeingShown) {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
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
                            this.alpha = 0.6f
                        }
                        .blur(15.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
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
                            .fillMaxWidth()
                            .padding(bottom = 30.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ResizableText(item?.name?.uppercase() ?: stringResource(R.string.name_not_found))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemRoute?.let {
                        DisplayItemRoute(route = it)
                    } ?: run {
                        CustomTextBold(text = stringResource(R.string.route_not_found))
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Display tags as chips
                    if (item?.tags?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item.tags.forEach { tag ->
                                TagChip(tag = tag.name)
                            }
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
        }
    }
}

@Composable
fun ResizableText(itemName: String?) {
    var textWidth by remember { mutableStateOf(0) }
    val defaultFontSize = 30.sp

    val minFontSize = 16.sp
    val maxFontSize = 30.sp

    val fontSize = remember(textWidth) {
        if (textWidth > 0) {
            val scaleFactor = textWidth / 300f
            val adjustedSize =
                (defaultFontSize.value * scaleFactor).coerceIn(minFontSize.value, maxFontSize.value)
            adjustedSize.sp
        } else {
            defaultFontSize
        }
    }

    Text(
        text = itemName?.uppercase() ?: stringResource(R.string.name_not_found),
        style = androidx.compose.ui.text.TextStyle(
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        ),
        color = Color.White,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .onSizeChanged { size ->
                textWidth = size.width
            },
        maxLines = 1,
        textAlign = TextAlign.Start
    )
}

@Composable
fun TagChip(tag: String) {
    val pastelColor = remember { generateRandomPastelColor() }

    androidx.compose.material3.AssistChip(
        onClick = { /* no onClick needed */ },
        label = {
            Text(
                text = tag.uppercase(),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = pastelColor,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = AssistChipDefaults.assistChipBorder(false)
    )
}


fun generateRandomPastelColor(): Color {
    val red = (100..255).random()
    val green = (100..255).random()
    val blue = (100..255).random()
    return Color(red, green, blue, 255)
}

@Composable
fun DisplayItemRoute(route: String) {
    val places = route.split("/")
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        places.forEachIndexed { index, place ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = place,
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            if (index < places.size - 1) {
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(4.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}
