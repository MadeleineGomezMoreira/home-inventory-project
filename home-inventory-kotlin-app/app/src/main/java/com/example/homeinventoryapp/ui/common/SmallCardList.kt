package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homeinventoryapp.domain.model.User


@Composable
fun <T> ListSmallCard(
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
fun <T> ClickableSmallCard(
    item: T,
    modifier: Modifier = Modifier,
    onItemClicked: (T) -> Unit,
    iconContent: @Composable () -> Unit = { DefaultIcon() },
    textContent: @Composable (T) -> String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .padding(horizontal = 15.dp)
            .clickable { onItemClicked(item) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.tertiary)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconContent()
                VerticalDivider(
                    color = Color.White,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.04F),
                )
                Text(
                    text = textContent(item),
                    fontSize = 27.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.35F)
                )
            }
        }
    }
}

@Preview
@Composable
fun ClickableSmallCardPreview() {
    ClickableSmallCard(
        item = User(
            id = 1,
            username = "Marina Luciana",
            email = "charlesevanshughes@myownpersonaldomain.com"
        ),
        onItemClicked = {},
        textContent = { it.username },
        iconContent = {
            DefaultIcon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User icon",
                size = 60.dp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    )
}

@Preview
@Composable
fun ListSmallCardPreview() {
    ListSmallCard(
        items = listOf(
            User(
                id = 1,
                username = "Marina Luciana",
                email = "john.mclean@examplepetstore.com"
            ),
            User(
                id = 2,
                username = "Nacho Silva",
                email = "john.mclean@examplepetstore.com"
            ),
            User(
                id = 3,
                username = "Juan Salazar",
                email = "james.s.sherman@example-pet-store.com"
            ),
        ),
        onItemClicked = {},
        itemContent = { user ->
            ClickableSmallCard(
                item = user,
                onItemClicked = {},
                textContent = { it.username },
                iconContent = {
                    DefaultIcon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User icon",
                        size = 60.dp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            )
        }

    )
}