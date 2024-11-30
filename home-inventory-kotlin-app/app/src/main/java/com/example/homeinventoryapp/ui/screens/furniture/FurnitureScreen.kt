package com.example.homeinventoryapp.ui.screens.furniture

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
import androidx.compose.material.icons.filled.FolderCopy
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
import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.ui.common.ClickableSmallCard
import com.example.homeinventoryapp.ui.common.CreateItemDialog
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultIcon
import com.example.homeinventoryapp.ui.common.ListSmallCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage

@Composable
fun FurnitureScreen(
    viewModel: FurnitureViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onCompartmentClicked: (Any?) -> Unit,
    furnId: Int,
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(furnId) {
        viewModel.handleEvent(FurnitureContract.FurnitureEvent.GetFurniture(furnId))
        viewModel.handleEvent(FurnitureContract.FurnitureEvent.GetFurnitureCompartments(furnId))
    }

    if (uiState.compartmentId != null) {
        val compId = uiState.compartmentId
        viewModel.handleEvent(FurnitureContract.FurnitureEvent.ClearCompartment)
        onCompartmentClicked(compId)
    }

    if (uiState.showCreateDialogue) {
        CreateItemDialog(
            onDismiss = { viewModel.handleEvent(FurnitureContract.FurnitureEvent.ClearDialogue) },
            onItemCreate = { compName ->
                viewModel.handleEvent(
                    FurnitureContract.FurnitureEvent.CreateCompartment(
                        compName,
                        furnId
                    )
                )
                viewModel.handleEvent(FurnitureContract.FurnitureEvent.ClearDialogue)
            },
            itemToCreateWord = "Compartment"
        )
    }

    FurnitureContent(
        furniture = uiState.furniture,
        compartments = uiState.compartments,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = { viewModel.handleEvent(FurnitureContract.FurnitureEvent.ErrorDisplayed) },
        onCompartmentClicked = { compartment ->
            viewModel.handleEvent(FurnitureContract.FurnitureEvent.CompartmentClicked(compartment as Compartment))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.handleEvent(FurnitureContract.FurnitureEvent.ShowDialogue)
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add compartment")
            }
        }

    )


}

@Composable
fun FurnitureContent(
    furniture: Furniture?,
    compartments: List<Compartment>? = emptyList(),
    error: String?,
    isLoading: Boolean = false,
    onCompartmentClicked: (Any?) -> Unit = {},
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
            CustomTextBold(text = furniture?.name ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            if (compartments?.isNotEmpty() == true) {
                Text(text = "Furniture", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                CompartmentListSmallCard(
                    compartments = compartments,
                    onCompartmentClicked = onCompartmentClicked
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
fun CompartmentListSmallCard(
    compartments: List<Compartment>,
    onCompartmentClicked: (Compartment) -> Unit,
) {
    ListSmallCard(
        items = compartments,
        onItemClicked = onCompartmentClicked
    ) { furniture ->
        ClickableSmallCard(
            item = furniture,
            onItemClicked = onCompartmentClicked,
            textContent = { it.name },
            iconContent = {
                DefaultIcon(
                    imageVector = Icons.Default.FolderCopy,
                    contentDescription = "User icon",
                    size = 60.dp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        )
    }
}