package com.example.homeinventoryapp.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.homeinventoryapp.domain.model.Item
import com.example.homeinventoryapp.ui.common.ClickableSquareCard
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.ListSquareCards
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    onItemClicked: (Any?) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    val homeId = UserSession.homeId

    if (uiState.searchWord.isNotEmpty()) {
        homeId?.let {
            viewModel.handleEvent(
                SearchContract.SearchEvent.GetSearch(
                    homeId,
                    uiState.searchWord
                )
            )
        }
    }

    if (uiState.itemId != null) {
        val itemId = uiState.itemId
        viewModel.handleEvent(SearchContract.SearchEvent.ClearItem)
        onItemClicked(itemId)
    }

    SearchContent(
        items = uiState.items,
        error = uiState.error,
        isLoading = uiState.isLoading,
        topBar = topBar,
        bottomNavigationBar = bottomNavigationBar,
        errorShown = {
            viewModel.handleEvent(SearchContract.SearchEvent.ErrorDisplayed)
        },
        onItemClicked = { item ->
            viewModel.handleEvent(SearchContract.SearchEvent.ItemClicked(item as Item))
        },
        onSearchWordChanged = { viewModel.handleEvent(SearchContract.SearchEvent.UpdateSearchWord(it)) },
        searchWord = uiState.searchWord
    )
}

@Composable
fun SearchContent(
    items: List<Item>? = emptyList(),
    error: String?,
    isLoading: Boolean = false,
    searchWord: String,
    onSearchWordChanged: (String) -> Unit,
    onItemClicked: (Any?) -> Unit = {},
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
            verticalArrangement = Arrangement.Top
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchWord,
                onValueChange = { onSearchWordChanged(it) },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 16.dp),
                placeholder = { Text(text = "Search for items...") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(16.dp))

            if (items?.isNotEmpty() == true) {
                Text(text = "Found Items", style = MaterialTheme.typography.titleLarge)
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
            } else {
                CustomTextBold(
                    text = "No items found.",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
        ShowSnackbarMessage(message = error, snackbarHostState = snackbarHostState) {
            errorShown()
        }
    }
}

@Composable
fun ItemIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.EmojiFoodBeverage,
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
fun ItemImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = "Default background image"
) {
    Image(
        painter = painterResource(id = R.drawable.van_gogh_shoes),
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}
