package com.example.homeinventoryapp.ui.screens.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.furniture.GetFurnitureByRoomUseCase
import com.example.homeinventoryapp.domain.usecases.furniture.SaveFurnitureUseCase
import com.example.homeinventoryapp.domain.usecases.room.GetRoomByIdUseCase
import com.example.homeinventoryapp.domain.usecases.room.UpdateRoomUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val getRoomById: GetRoomByIdUseCase,
    private val getFurnitureByRoom: GetFurnitureByRoomUseCase,
    private val saveFurniture: SaveFurnitureUseCase,
    private val updateRoom: UpdateRoomUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<RoomContract.RoomState> by lazy {
        MutableStateFlow(RoomContract.RoomState())
    }

    val state: StateFlow<RoomContract.RoomState> = _state.asStateFlow()

    fun handleEvent(event: RoomContract.RoomEvent) {
        when (event) {
            is RoomContract.RoomEvent.GetRoom -> getRoom(event.roomId)
            is RoomContract.RoomEvent.GetRoomFurniture -> getRoomFurniture(event.roomId)
            is RoomContract.RoomEvent.FurnitureClicked -> _state.value = _state.value.copy(
                furnitureId = event.furniture.id
            )

            is RoomContract.RoomEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is RoomContract.RoomEvent.ClearFurniture -> _state.value =
                _state.value.copy(furnitureId = null)

            RoomContract.RoomEvent.ClearCreateDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = false)

            RoomContract.RoomEvent.ShowCreateDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = true)

            is RoomContract.RoomEvent.CreateFurniture -> createFurniture(
                event.furnitureName,
                event.roomId
            )

            RoomContract.RoomEvent.ClearEditDialogue -> _state.value =
                _state.value.copy(showEditDialogue = false)

            RoomContract.RoomEvent.ShowEditDialogue -> _state.value =
                _state.value.copy(showEditDialogue = true)

            is RoomContract.RoomEvent.EditRoom -> editRoom(event.roomName, event.roomId, event.homeId)
        }
    }

    private fun editRoom(roomName: String, roomId: Int, homeId: Int) {
        viewModelScope.launch {
            updateRoom.invoke(roomId, roomName, homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                room = result.data
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

    private fun createFurniture(name: String, roomId: Int) {
        viewModelScope.launch {
            saveFurniture.invoke(name, roomId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                furnitureId = result.data?.id
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

    private fun getRoomFurniture(roomId: Int) {
        viewModelScope.launch {
            getFurnitureByRoom.invoke(roomId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                furniture = result.data ?: emptyList()
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

    private fun getRoom(roomId: Int) {
        viewModelScope.launch {
            getRoomById.invoke(roomId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                room = result.data
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