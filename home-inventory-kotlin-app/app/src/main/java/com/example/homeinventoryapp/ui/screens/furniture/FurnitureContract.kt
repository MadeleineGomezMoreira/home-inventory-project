package com.example.homeinventoryapp.ui.screens.furniture

import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Furniture

class FurnitureContract {
    data class FurnitureState(
        val furniture: Furniture? = null,
        val compartments: List<Compartment> = emptyList(),
        val compartmentId: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showCreateDialogue: Boolean = false,
        val showEditDialogue: Boolean = false,
    )

    sealed class FurnitureEvent {
        data class GetFurniture(val furnitureId: Int) : FurnitureEvent()
        data class GetFurnitureCompartments(val furnitureId: Int) : FurnitureEvent()
        data class CreateCompartment(val name: String, val furnId: Int) : FurnitureEvent()
        data class EditFurniture(val furnitureName: String, val furnId: Int, val roomId: Int) :
            FurnitureEvent()

        data class CompartmentClicked(val compartment: Compartment) : FurnitureEvent()
        data object ErrorDisplayed : FurnitureEvent()
        data object ClearCompartment : FurnitureEvent()
        data object ShowCreateDialogue : FurnitureEvent()
        data object ClearCreateDialogue : FurnitureEvent()
        data object ShowEditDialogue : FurnitureEvent()
        data object ClearEditDialogue : FurnitureEvent()
    }
}