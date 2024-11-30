package com.example.homeinventoryapp.ui.screens.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.EditItemAndTagsDialog
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage

@Composable
fun ItemScreen(
    viewModel: ItemViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    itemId: Int,
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.handleEvent(ItemContract.ItemEvent.GetItem(itemId))
        viewModel.handleEvent(ItemContract.ItemEvent.GetItemRoute(itemId))
    }

    if (uiState.showEditDialogue) {
        EditItemAndTagsDialog(item = uiState.item,
            onDismiss = { viewModel.handleEvent(ItemContract.ItemEvent.ClearDialogue) },
            onItemCreate = { viewModel.handleEvent(ItemContract.ItemEvent.UpdateItem(it)) })
    }

    ItemContent(item = uiState.item,
        itemRoute = uiState.itemRoute,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(ItemContract.ItemEvent.ErrorDisplayed)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.handleEvent(ItemContract.ItemEvent.ShowDialogue)
            }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit item")
            }
        })

}

@Composable
fun ItemContent(
    item: ItemDetail?,
    itemRoute: String?,
    error: String?,
    isLoading: Boolean = false,
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
            CustomTextBold(text = item?.name ?: "Name not found")
            Spacer(modifier = Modifier.height(8.dp))
            itemRoute?.let {
                DisplayItemRoute(route = it)
            } ?: run {
                CustomTextBold(text = "Route not found")
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
fun DisplayItemRoute(route: String) {
    val places = route.split("/")
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        places.forEachIndexed { index, place ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = place,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            if (index < places.size - 1) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(2.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }
}