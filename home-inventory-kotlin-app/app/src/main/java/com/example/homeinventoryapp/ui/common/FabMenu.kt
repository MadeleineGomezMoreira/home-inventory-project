package com.example.homeinventoryapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionMenuAddEdit(
    onAddElementClicked: () -> Unit,
    onEditElementClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp)) {
        // Main FAB
        FloatingActionButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More actions")
        }

        if (expanded) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
            ) {
                FloatingActionButton(onClick = {
                    expanded = false
                    onEditElementClicked()
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit element")
                }

                FloatingActionButton(onClick = {
                    expanded = false
                    onAddElementClicked()
                }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add element")
                }
                FloatingActionButton(onClick = { expanded = false }) {
                    Icon(
                        imageVector = Icons.Default.CloseFullscreen,
                        contentDescription = "Collapse FAB menu"
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingActionMenuEditMove(
    onMoveElementClicked: () -> Unit,
    onEditElementClicked: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp)) {
        // Main FAB
        FloatingActionButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More actions")
        }

        if (expanded) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
            ) {
                FloatingActionButton(onClick = {
                    expanded = false
                    onEditElementClicked()
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit element")
                }

                FloatingActionButton(onClick = {
                    expanded = false
                    onMoveElementClicked()
                }) {
                    Icon(imageVector = Icons.Filled.Upgrade, contentDescription = "Move element")
                }
                FloatingActionButton(onClick = { expanded = false }) {
                    Icon(
                        imageVector = Icons.Default.CloseFullscreen,
                        contentDescription = "Collapse FAB menu"
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingActionMenuEditDeleteAdd(
    onDeleteElementClicked: () -> Unit,
    onEditElementClicked: () -> Unit,
    onAddHomeMember: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp)) {
        // Main FAB
        FloatingActionButton(onClick = { expanded = !expanded }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More actions")
        }

        if (expanded) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
            ) {
                FloatingActionButton(onClick = {
                    expanded = false
                    onAddHomeMember()
                }) {
                    Icon(imageVector = Icons.Default.PersonAddAlt, contentDescription = "Add member")
                }

                FloatingActionButton(onClick = {
                    expanded = false
                    onEditElementClicked()
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit element")
                }

                FloatingActionButton(onClick = {
                    expanded = false
                    onDeleteElementClicked()
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete element")
                }
                FloatingActionButton(onClick = { expanded = false }) {
                    Icon(
                        imageVector = Icons.Default.CloseFullscreen,
                        contentDescription = "Collapse FAB menu"
                    )
                }
            }
        }
    }
}
