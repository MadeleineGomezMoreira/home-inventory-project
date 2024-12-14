package com.example.homeinventoryapp.ui.screens.furniture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.compartment.GetCompartmentsByFurnitureUseCase
import com.example.homeinventoryapp.domain.usecases.compartment.SaveCompartmentUseCase
import com.example.homeinventoryapp.domain.usecases.furniture.GetFurnitureByIdUseCase
import com.example.homeinventoryapp.domain.usecases.furniture.UpdateFurnitureUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FurnitureViewModel @Inject constructor(
    private val getFurnitureById: GetFurnitureByIdUseCase,
    private val getCompartmentsByFurniture: GetCompartmentsByFurnitureUseCase,
    private val saveCompartment: SaveCompartmentUseCase,
    private val updateFurniture: UpdateFurnitureUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<FurnitureContract.FurnitureState> by lazy {
        MutableStateFlow(FurnitureContract.FurnitureState())
    }

    val state: StateFlow<FurnitureContract.FurnitureState> = _state.asStateFlow()

    fun handleEvent(event: FurnitureContract.FurnitureEvent) {
        when (event) {
            is FurnitureContract.FurnitureEvent.GetFurniture -> getFurniture(event.furnitureId)
            is FurnitureContract.FurnitureEvent.GetFurnitureCompartments -> getCompartments(event.furnitureId)
            is FurnitureContract.FurnitureEvent.CreateCompartment -> createCompartment(
                event.name, event.furnId
            )

            is FurnitureContract.FurnitureEvent.CompartmentClicked -> _state.value =
                _state.value.copy(compartmentId = event.compartment.id)

            is FurnitureContract.FurnitureEvent.ClearCompartment -> _state.value =
                _state.value.copy(compartmentId = null)

            is FurnitureContract.FurnitureEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            FurnitureContract.FurnitureEvent.ClearCreateDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = false)

            FurnitureContract.FurnitureEvent.ShowCreateDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = true)

            FurnitureContract.FurnitureEvent.ClearEditDialogue -> _state.value =
                _state.value.copy(showEditDialogue = false)

            FurnitureContract.FurnitureEvent.ShowEditDialogue -> _state.value =
                _state.value.copy(showEditDialogue = true)

            is FurnitureContract.FurnitureEvent.EditFurniture -> editFurniture(
                event.furnitureName, event.furnId, event.roomId
            )
        }
    }

    private fun editFurniture(furnitureName: String, furnId: Int, roomId: Int) {
        viewModelScope.launch {
            updateFurniture.invoke(furnId, furnitureName, roomId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false, error = null, furniture = result.data
                        )
                    }

                    is NetworkResult.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false, error = result.message
                        )
                    }
                }
            }
        }
    }

    private fun getCompartments(furnId: Int) {
        viewModelScope.launch {
            getCompartmentsByFurniture(furnId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false, compartments = result.data ?: emptyList()
                        )
                    }

                    is NetworkResult.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false, error = result.message
                        )
                    }
                }
            }
        }
    }

    private fun getFurniture(furnId: Int) {
        viewModelScope.launch {
            getFurnitureById.invoke(furnId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false, furniture = result.data
                        )
                    }

                    is NetworkResult.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false, error = result.message
                        )
                    }
                }
            }
        }
    }

    private fun createCompartment(name: String, furnId: Int) {
        viewModelScope.launch {
            saveCompartment.invoke(name, furnId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false, compartmentId = result.data?.id
                        )
                    }

                    is NetworkResult.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false, error = result.message
                        )
                    }
                }
            }
        }
    }

}