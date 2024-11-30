package com.example.homeinventoryapp.ui.common

import android.content.ClipData.Item
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.domain.model.ItemTag


@Composable
fun CreateItemDialog(
    onDismiss: () -> Unit,
    onItemCreate: (String) -> Unit,
    itemToCreateWord : String? = "Item"
) {
    var itemName by remember { mutableStateOf(TextFieldValue()) }

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
                    Text(text = "Create new $itemToCreateWord", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    placeholder = { Text(text = "Enter a new name") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onItemCreate(itemName.text)
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

@Composable
fun CreateItemAndTagsDialog(
    onDismiss: () -> Unit,
    onItemCreate: (String, List<String>) -> Unit // Updated to include tags
) {
    var itemName by remember { mutableStateOf(TextFieldValue()) }
    var tagName by remember { mutableStateOf(TextFieldValue()) }
    val tags = remember { mutableStateOf(mutableListOf<String>()) }

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
                // Header Row
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
                    Text(text = "Create Item", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Item Name Input
                TextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    placeholder = { Text(text = "Enter a new item name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tag Input Section
                Text(text = "Tags", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = tagName,
                        onValueChange = { tagName = it },
                        placeholder = { Text(text = "Enter tag name") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (tagName.text.isNotBlank()) {
                            tags.value.add(tagName.text.trim())
                            tagName = TextFieldValue("") // Clear tag input
                        }
                    }) {
                        Text(text = "ADD")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display Added Tags
                Column {
                    tags.value.forEach { tag ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = { tags.value.remove(tag) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove tag"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Create Button
                Button(
                    onClick = {
                        onItemCreate(itemName.text, tags.value)
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
@Composable
fun EditItemAndTagsDialog(
    item: ItemDetail?,
    onDismiss: () -> Unit,
    onItemCreate: (ItemDetail) -> Unit
) {
    val initialHomeId = item?.tags?.firstOrNull()?.homeId ?: 0
    var tagName by remember { mutableStateOf(TextFieldValue()) }
    var itemName by remember { mutableStateOf(item?.name?.let { TextFieldValue(it) }) }
    val tags = remember { item?.tags?.let { mutableStateOf(it.toMutableList()) } }

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
                // Header Row
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
                    Text(text = "Edit Item", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Item Name Input
                itemName?.let {
                    TextField(
                        value = it,
                        onValueChange = { itemName = it },
                        placeholder = { Text(text = "Enter item name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tag Input Section
                Text(text = "Tags", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = tagName,
                        onValueChange = { tagName = it },
                        placeholder = { Text(text = "Enter tag name") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (tagName.text.isNotBlank()) {
                            tags?.value?.add(
                                ItemTag(
                                    id = 0, // Temporary ID for new tags
                                    name = tagName.text.trim(),
                                    homeId = initialHomeId
                                )
                            )
                            tagName = TextFieldValue("") // Clear tag input
                        }
                    }) {
                        Text(text = "ADD")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display Added Tags
                Column {
                    tags?.value?.forEach { tag ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = tag.name,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = {
                                tags.value =
                                    tags.value.filter { it != tag }.toMutableList()
                            })  {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "REMOVE"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Edit Button
                Button(
                    onClick = {
                        val updatedItem = itemName?.text?.let {
                            item?.copy(
                                name = it,
                                tags = tags?.value ?: emptyList()
                            )
                        }
                        if (updatedItem != null) {
                            onItemCreate(updatedItem)
                        }
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "EDIT")
                }
            }
        }
    }
}
