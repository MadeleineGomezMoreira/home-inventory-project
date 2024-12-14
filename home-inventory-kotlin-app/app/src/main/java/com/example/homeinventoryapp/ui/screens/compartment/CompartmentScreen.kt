package com.example.homeinventoryapp.ui.screens.compartment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Item
import com.example.homeinventoryapp.ui.common.ClickableSquareCard
import com.example.homeinventoryapp.ui.common.CreateItemAndTagsDialog
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.ListSquareCards
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.screens.search.ItemIcon
import com.example.homeinventoryapp.ui.screens.search.ItemImage

@Composable
fun CompartmentScreen(
    viewModel: CompartmentViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onItemClicked: (Any?) -> Unit,
    compId: Int,
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(compId) {
        viewModel.handleEvent(CompartmentContract.CompartmentEvent.GetCompartment(compId))
        viewModel.handleEvent(CompartmentContract.CompartmentEvent.GetCompartmentItems(compId))
    }

    if (uiState.itemId != null) {
        val itemId = uiState.itemId
        viewModel.handleEvent(CompartmentContract.CompartmentEvent.ClearItem)
        onItemClicked(itemId)
    }

    if (uiState.showCreateDialogue) {
        CreateItemAndTagsDialog(
            onDismiss = { viewModel.handleEvent(CompartmentContract.CompartmentEvent.ClearDialogue) },
            onItemCreate = { itemName, tags ->
                viewModel.handleEvent(
                    CompartmentContract.CompartmentEvent.CreateItem(
                        itemName,
                        compId,
                        tags
                    )
                )
                viewModel.handleEvent(CompartmentContract.CompartmentEvent.ClearDialogue)
            },
        )
    }

    CompartmentContent(
        compartment = uiState.compartment,
        items = uiState.items,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = { viewModel.handleEvent(CompartmentContract.CompartmentEvent.ErrorDisplayed) },
        onItemClicked = { item ->
            viewModel.handleEvent(CompartmentContract.CompartmentEvent.ItemClicked(item as Item))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.handleEvent(CompartmentContract.CompartmentEvent.ShowDialogue)
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
            }
        }
    )
}


@Composable
fun CompartmentContent(
    compartment: Compartment?,
    items: List<Item>? = emptyList(),
    error: String?,
    isLoading: Boolean = false,
    onItemClicked: (Any?) -> Unit = {},
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
        floatingActionButton = floatingActionButton
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
            CustomTextBold(text = compartment?.name ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            if (items?.isNotEmpty() == true) {
                Text(text = "Items", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                ListSquareCards(
                    items = items,
                    onItemClicked = onItemClicked,
                ) { item ->
                    ClickableSquareCard(
                        item = item,
                        onItemClicked = onItemClicked,
                        textContent = { it.name },
                        iconContent = { ItemIcon() },
                        imageContent = { ItemImage() }
                    )
                }
            }
            if (items.isNullOrEmpty()) {
                CustomTextBold(
                    text = "No items found.",
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