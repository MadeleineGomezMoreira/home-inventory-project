package com.example.homeinventoryapp.ui.screens.myhomes

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.ui.common.ClickableBigCard
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.ListBigCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession

@Composable
fun MyHomesScreen(
    viewModel: MyHomesViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onHomeClicked: (Any?) -> Unit,
) {
    val userId = UserSession.userId
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.handleEvent(MyHomesContract.MyHomesEvent.GetHomes(it))
        }
    }

    if (uiState.homeId != null) {
        val homeId = uiState.homeId
        viewModel.handleEvent(MyHomesContract.MyHomesEvent.ClearHome)
        onHomeClicked(homeId)
    }

    MyHomesContent(
        ownerHomes = uiState.ownerHomes,
        error = uiState.error,
        isLoading = uiState.isLoading,
        onHomeClicked = { home ->
            viewModel.handleEvent(MyHomesContract.MyHomesEvent.HomeClicked(home as Home))
        },
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(MyHomesContract.MyHomesEvent.ErrorDisplayed)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.handleEvent(MyHomesContract.MyHomesEvent.ShowDialogue)
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Home")
            }
        }
    )
    if (uiState.showCreateDialogue) {
        CreateHomeDialog(
            onDismiss = { viewModel.handleEvent(MyHomesContract.MyHomesEvent.ClearDialogue) },
            onHomeCreate = { homeName ->
                viewModel.handleEvent(MyHomesContract.MyHomesEvent.CreateHome(homeName, userId))
                viewModel.handleEvent(MyHomesContract.MyHomesEvent.ClearDialogue)
            }
        )
    }
}

@Composable
fun MyHomesContent(
    ownerHomes: List<Home>? = emptyList(),
    memberHomes: List<Home>? = emptyList(),
    error: String?,
    isLoading: Boolean = false,
    onHomeClicked: (Any?) -> Unit = {},
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (ownerHomes?.isNotEmpty() == true) {
                Text(text = "Owned Homes", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                HomeListBigCard(
                    homes = ownerHomes,
                    onHomeClicked = onHomeClicked
                )
            }
            if (memberHomes?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Member Homes", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                HomeListBigCard(
                    homes = memberHomes,
                    onHomeClicked = onHomeClicked
                )
            }
            if ((ownerHomes.isNullOrEmpty() && memberHomes.isNullOrEmpty())) {
                CustomTextBold(
                    text = "No homes found.",
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
fun HomeListBigCard(
    homes: List<Home>,
    onHomeClicked: (Home) -> Unit
) {
    ListBigCard(
        items = homes,
        onItemClicked = onHomeClicked
    ) { home ->
        ClickableBigCard(
            item = home,
            onItemClicked = onHomeClicked,
            textContent = { it.name }
        )
    }
}

@Composable
fun CreateHomeDialog(
    onDismiss: () -> Unit,
    onHomeCreate: (String) -> Unit
) {
    var homeName by remember { mutableStateOf(TextFieldValue()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Create Home", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = homeName,
                    onValueChange = { homeName = it },
                    placeholder = { Text(text = "Enter home name") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onHomeCreate(homeName.text)
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "CREATE")
                }
            }
        }
    }
}
