package com.example.homeinventoryapp.ui.screens.compartment

import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Item

class CompartmentContract {

    data class CompartmentState(
        val compartment: Compartment? = null,
        val items: List<Item> = emptyList(),
        val itemId: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showCreateDialogue: Boolean = false
    )

    sealed class CompartmentEvent {
        data class GetCompartment(val compId: Int) : CompartmentEvent()
        data class GetCompartmentItems(val compId: Int) : CompartmentEvent()
        data class CreateItem(val itemName: String, val compId: Int, val tags: List<String>) :
            CompartmentEvent()

        data class ItemClicked(val item: Item) : CompartmentEvent()
        data object ErrorDisplayed : CompartmentEvent()
        data object ClearItem : CompartmentEvent()
        data object ShowDialogue : CompartmentEvent()
        data object ClearDialogue : CompartmentEvent()
    }

}