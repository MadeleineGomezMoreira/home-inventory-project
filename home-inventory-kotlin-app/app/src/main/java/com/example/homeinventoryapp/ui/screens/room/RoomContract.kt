package com.example.homeinventoryapp.ui.screens.room

import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.domain.model.Room

class RoomContract {

    data class RoomState(
        val room: Room? = null,
        val furniture: List<Furniture> = emptyList(),
        val furnitureId: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showCreateDialogue: Boolean = false
    )

    sealed class RoomEvent {
        data class GetRoom(val roomId: Int) : RoomEvent()
        data class GetRoomFurniture(val roomId: Int) : RoomEvent()
        data class CreateFurniture(val furnitureName: String, val roomId: Int) : RoomEvent()
        data class FurnitureClicked(val furniture: Furniture) : RoomEvent()
        data object ErrorDisplayed : RoomEvent()
        data object ClearFurniture : RoomEvent()
        data object ShowDialogue : RoomEvent()
        data object ClearDialogue : RoomEvent()
    }

}