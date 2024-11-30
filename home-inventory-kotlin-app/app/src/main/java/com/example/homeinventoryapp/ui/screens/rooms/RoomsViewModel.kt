package com.example.homeinventoryapp.ui.screens.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.room.GetRoomsByHomeUseCase
import com.example.homeinventoryapp.domain.usecases.room.SaveRoomUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val getRoomsByHome: GetRoomsByHomeUseCase,
    private val createRoom: SaveRoomUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<RoomsContract.RoomsState> by lazy {
        MutableStateFlow(RoomsContract.RoomsState())
    }

    val state: StateFlow<RoomsContract.RoomsState> = _state.asStateFlow()

    fun handleEvent(event: RoomsContract.RoomsEvent) {
        when (event) {
            is RoomsContract.RoomsEvent.GetRooms -> getRooms(event.homeId)
            is RoomsContract.RoomsEvent.CreateRoom -> saveRoom(event.homeId, event.roomName)
            is RoomsContract.RoomsEvent.RoomClicked ->
                _state.value = _state.value.copy(roomId = event.room.id)

            is RoomsContract.RoomsEvent.ErrorDisplayed ->
                _state.value = _state.value.copy(error = null)

            is RoomsContract.RoomsEvent.ClearRoom ->
                _state.value = _state.value.copy(roomId = null)

            is RoomsContract.RoomsEvent.ShowDialogue ->
                _state.value = _state.value.copy(showCreateDialogue = true)

            is RoomsContract.RoomsEvent.ClearDialogue ->
                _state.value = _state.value.copy(showCreateDialogue = false)
        }
    }

    private fun saveRoom(homeId: Int, roomName: String) {
        viewModelScope.launch {
            createRoom.invoke(roomName, homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                roomId = result.data?.id
                            )
                        }

                        is NetworkResult.Loading -> {
                            _state.value = _state.value.copy(
                                isLoading = true
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
        }
    }

    private fun getRooms(homeId: Int) {
        viewModelScope.launch {
            getRoomsByHome.invoke(homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                rooms = result.data ?: emptyList()
                            )
                        }

                        is NetworkResult.Loading -> {
                            _state.value = _state.value.copy(
                                isLoading = true
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
        }
    }


}