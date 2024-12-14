package com.example.homeinventoryapp.ui.screens.item

import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.domain.model.Room

class ItemContract {

    data class ItemState(
        val item: ItemDetail? = null,
        val rooms: List<Room>? = emptyList(),
        val furniture: List<Furniture>? = emptyList(),
        val compartments: List<Compartment>? = emptyList(),
        val itemRoute: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showEditDialogue: Boolean = false,
        val showMoveDialogue: Boolean = false,
        val itemMoved: Boolean = false,
    )

    sealed class ItemEvent {
        data class GetItem(val itemId: Int) : ItemEvent()
        data class GetRooms(val homeId: Int) : ItemEvent()
        data class GetFurniture(val roomId: Int) : ItemEvent()
        data class GetCompartments(val furnitureId: Int) : ItemEvent()
        data class MoveItem(val itemId: Int, val compId: Int) : ItemEvent()
        data class GetItemRoute(val itemId: Int) : ItemEvent()
        data class UpdateItem(val item: ItemDetail) : ItemEvent()
        data object ErrorDisplayed : ItemEvent()
        data object ShowEditDialogue : ItemEvent()
        data object ShowMoveDialogue : ItemEvent()
        data object ClearDialogs : ItemEvent()
        data object ClearItemMoved : ItemEvent()
        data object ClearDialogData : ItemEvent()
    }

}