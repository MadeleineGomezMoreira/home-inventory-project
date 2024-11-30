package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.domain.model.Home


@Composable
fun <T> ListBigCard(
    modifier: Modifier = Modifier,
    items: List<T> = emptyList(),
    onItemClicked: (T) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Box(modifier = Modifier.clickable { onItemClicked(item) }) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun <T> ClickableBigCard(
    item: T,
    modifier: Modifier = Modifier,
    onItemClicked: (T) -> Unit,
    iconContent: @Composable () -> Unit = { DefaultIcon() },
    imageContent: @Composable () -> Unit = { DefaultImage() },
    textContent: @Composable (T) -> String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
            .padding(horizontal = 15.dp)
            .clickable { onItemClicked(item) }
    ) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {
                imageContent()
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    iconContent()
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp),
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = textContent(item),
                        fontSize = 27.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DefaultIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.Home,
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
fun DefaultImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = "Default background image"
) {
    Image(
        painter = painterResource(id = R.drawable.van_gogh_home),
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun ClickableBigCardPreview() {
    ClickableBigCard(
        item = Home(
            id = 1,
            name = "Home name",
            owner = 1
        ),
        onItemClicked = {},
        textContent = { it.name }
    )
}

@Preview
@Composable
fun ListBigCardPreview() {
    ListBigCard(
        items = listOf(
            Home(
                id = 1,
                name = "Home name 1",
                owner = 1
            ),
            Home(
                id = 2,
                name = "Home name 2",
                owner = 1
            ),
            Home(
                id = 3,
                name = "Home name 3",
                owner = 2
            ),
        ),
        onItemClicked = {},
        itemContent = { home ->
            ClickableBigCard(
                item = home,
                onItemClicked = {},
                textContent = { it.name }
            )
        }
    )
}