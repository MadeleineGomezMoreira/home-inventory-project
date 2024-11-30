package com.example.homeinventoryapp.ui.screens.item

import com.example.homeinventoryapp.domain.model.ItemDetail

class ItemContract {

    data class ItemState(
        val item: ItemDetail? = null,
        val itemRoute : String? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showEditDialogue: Boolean = false,
    )

    sealed class ItemEvent {
        data class GetItem(val itemId: Int) : ItemEvent()
        data class GetItemRoute(val itemId: Int) : ItemEvent()
        data class UpdateItem(val item: ItemDetail) : ItemEvent()
        data object ErrorDisplayed : ItemEvent()
        data object ShowDialogue : ItemEvent()
        data object ClearDialogue : ItemEvent()
    }

}