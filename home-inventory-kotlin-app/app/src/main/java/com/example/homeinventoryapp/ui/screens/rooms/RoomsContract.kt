package com.example.homeinventoryapp.ui.screens.rooms

import com.example.homeinventoryapp.domain.model.Room

class RoomsContract {

    data class RoomsState(
        val rooms: List<Room> = emptyList(),
        val roomId: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showCreateDialogue: Boolean = false
    )

    sealed class RoomsEvent {
        data class GetRooms(val homeId: Int) : RoomsEvent()
        data class RoomClicked(val room: Room) : RoomsEvent()
        data class CreateRoom(val homeId: Int, val roomName: String) : RoomsEvent()
        data object ErrorDisplayed : RoomsEvent()
        data object ClearRoom : RoomsEvent()
        data object ShowDialogue : RoomsEvent()
        data object ClearDialogue : RoomsEvent()
    }

}