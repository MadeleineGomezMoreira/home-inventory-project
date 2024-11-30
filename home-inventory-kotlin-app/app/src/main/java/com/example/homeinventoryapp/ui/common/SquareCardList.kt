package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homeinventoryapp.domain.model.Item

@Composable
fun <T> ListSquareCards(
    modifier: Modifier = Modifier,
    items: List<T> = emptyList(),
    onItemClicked: (T) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Box(modifier = Modifier.clickable { onItemClicked(item) }) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun <T> ClickableSquareCard(
    item: T,
    modifier: Modifier = Modifier,
    onItemClicked: (T) -> Unit = {},
    iconContent: @Composable () -> Unit = { DefaultIcon() },
    imageContent: @Composable () -> Unit = { DefaultImage() },
    textContent: @Composable (T) -> String
) {
    Card(
        modifier = modifier
            .aspectRatio(1f) // Square shape
            .padding(8.dp)
            .clickable { onItemClicked(item) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f) // Take most of the space
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {
                imageContent() // Image at the top
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart) // Icon in the top-left corner
                        .padding(8.dp)
                ) {
                    iconContent()
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.4f) // Bottom section
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    text = textContent(item),
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center) // Center the text
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ListSquareCardsPreview() {
    ListSquareCards(
        items = listOf(
            Item(id = 1, name = "Red sneakers", compId = 1),
            Item(id = 2, name = "Sandals", compId = 1),
            Item(id = 3, name = "Boots", compId = 2),
            Item(id = 4, name = "Slippers", compId = 2),
            Item(id = 5, name = "White shoes", compId = 3),
            Item(id = 6, name = "FlipFlops", compId = 3)
        ),
        onItemClicked = {},
        itemContent = { item ->
            ClickableSquareCard(
                item = item,
                onItemClicked = {},
                textContent = { it.name }
            )
        }
    )
}

@Preview
@Composable
fun ClickableSquareCardPreview() {
    ClickableSquareCard(
        item = Item(id = 1, name = "Red sneakers", compId = 1),
        textContent = { it.name }
    )
}