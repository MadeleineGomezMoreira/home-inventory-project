package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.domain.model.InvitationInfo
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.domain.model.ItemTag
import com.example.homeinventoryapp.domain.model.Room
import com.example.homeinventoryapp.ui.common.di.UserSession


@Composable
fun CreateItemDialog(
    onDismiss: () -> Unit,
    onItemCreate: (String) -> Unit,
    itemToCreateWord: String? = "Item"
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
                    Text(
                        text = "Create new $itemToCreateWord",
                        style = MaterialTheme.typography.titleMedium
                    )
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
fun MoveItemDialog(
    onDismiss: () -> Unit,
    onMoveItem: (Int) -> Unit,
    rooms: List<Room>,
    furniture: List<Furniture>,
    compartments: List<Compartment>,
    onSelectedRoom: (Int) -> Unit,
    onSelectedFurniture: (Int) -> Unit,
) {
    var selectedRoomId by remember { mutableStateOf<Int?>(null) }
    var selectedFurnitureId by remember { mutableStateOf<Int?>(null) }
    var selectedCompartmentId by remember { mutableStateOf<Int?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Move Item") },
        text = {
            Column {
                // Room ComboBox
                Text(text = "Select Room")
                DropdownMenuField(
                    items = rooms,
                    onItemSelected = { room ->
                        selectedRoomId = room.id
                        onSelectedRoom(room.id)
                    },
                    label = { it.name },
                    enabled = rooms.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Furniture ComboBox
                Text(text = "Select Furniture")
                DropdownMenuField(
                    items = furniture,
                    onItemSelected = { furniture ->
                        selectedFurnitureId = furniture.id
                        onSelectedFurniture(furniture.id)
                    },
                    label = { it.name },
                    enabled = selectedRoomId != null && furniture.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Compartment ComboBox
                Text(text = "Select Compartment")
                DropdownMenuField(
                    items = compartments,
                    onItemSelected = { compartment ->
                        selectedCompartmentId = compartment.id
                    },
                    label = { it.name },
                    enabled = selectedFurnitureId != null && compartments.isNotEmpty()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onMoveItem(selectedCompartmentId ?: 0)
                    selectedRoomId = null
                    selectedFurnitureId = null
                    selectedCompartmentId = null
                },
                enabled = selectedCompartmentId != null
            ) {
                Text(text = "MOVE")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "CANCEL")
            }
        }
    )
}

@Composable
fun <T> DropdownMenuField(
    items: List<T>,
    onItemSelected: (T) -> Unit,
    label: (T) -> String,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            label = { Text("Select an option") },
            enabled = enabled,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable(enabled = enabled) {
                        expanded = !expanded
                    }
                )
            },
            modifier = Modifier
                .clickable(enabled = enabled) { expanded = true } // Ensures clickable behavior
                .fillMaxWidth(),
            readOnly = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                        selectedLabel = label(item)
                    },
                    text = {
                        Text(
                            text = label(item),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }
}


@Composable
fun InvitationDialog(
    invitationInfo: InvitationInfo?,
    onDismiss: () -> Unit,
    onAcceptInvitation: (Int) -> Unit,
    onDeclineInvitation: (Int) -> Unit,
) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, // Distribute content evenly
                        verticalAlignment = Alignment.CenterVertically // Center items vertically
                    ) {
                        if (invitationInfo != null) {
                            Text(
                                text = "Invitation from ${invitationInfo.inviterName}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f) // Take available horizontal space
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "You were invited to: ${invitationInfo.homeName}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f) // Take available horizontal space
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            invitationInfo?.let { onDeclineInvitation(it.id) }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Decline",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Decline")
                    }

                    Button(
                        onClick = {
                            invitationInfo?.let { onAcceptInvitation(it.id) }
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Accept",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Accept")
                    }
                }
            }
        }
    }
}

@Composable
fun SendInvitationDialog(
    onDismiss: () -> Unit,
    onSendInvitation: (String) -> Unit,
) {
    var username by remember { mutableStateOf(TextFieldValue()) }

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
                    Text(
                        text = "Invite a user to your home",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text(text = "Enter the name of the user you want to invite") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSendInvitation(username.text)
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "SEND")
                }
            }
        }
    }
}

@Composable
fun EditItemDialog(
    onDismiss: () -> Unit,
    onItemEdit: (String) -> Unit,
    itemToEditWord: String? = "Item"
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
                    Text(
                        text = "Edit $itemToEditWord's name",
                        style = MaterialTheme.typography.titleMedium
                    )
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
                        onItemEdit(itemName.text)
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
    onItemEdit: (ItemDetail) -> Unit
) {
    val initialHomeId = UserSession.homeId ?: 0
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
                            }) {
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
                            onItemEdit(updatedItem)
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
