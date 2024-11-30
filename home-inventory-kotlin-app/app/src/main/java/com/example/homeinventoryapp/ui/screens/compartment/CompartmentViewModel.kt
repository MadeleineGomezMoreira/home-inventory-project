package com.example.homeinventoryapp.ui.screens.compartment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.domain.model.ItemTag
import com.example.homeinventoryapp.domain.usecases.compartment.GetCompartmentByIdUseCase
import com.example.homeinventoryapp.domain.usecases.item.GetItemsByCompartmentUseCase
import com.example.homeinventoryapp.domain.usecases.item.SaveItemUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompartmentViewModel @Inject constructor(
    private val getCompartmentById: GetCompartmentByIdUseCase,
    private val getCompartmentItems: GetItemsByCompartmentUseCase,
    private val saveItem: SaveItemUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<CompartmentContract.CompartmentState> by lazy {
        MutableStateFlow(CompartmentContract.CompartmentState())
    }

    val state: StateFlow<CompartmentContract.CompartmentState> = _state.asStateFlow()

    fun handleEvent(event: CompartmentContract.CompartmentEvent) {
        when (event) {
            is CompartmentContract.CompartmentEvent.getCompartment -> getCompartment(event.compId)
            is CompartmentContract.CompartmentEvent.getCompartmentItems -> getItems(event.compId)
            is CompartmentContract.CompartmentEvent.CreateItem -> createItem(
                event.itemName,
                event.compId,
                event.tags
            )

            is CompartmentContract.CompartmentEvent.ClearItem -> _state.value =
                _state.value.copy(itemId = null)

            is CompartmentContract.CompartmentEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)
            is CompartmentContract.CompartmentEvent.ItemClicked -> _state.value = _state.value.copy(
                itemId = event.item.id
            )
            is CompartmentContract.CompartmentEvent.ShowDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = true)
            is CompartmentContract.CompartmentEvent.ClearDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = false)

        }
    }

    private fun createItem(itemName: String, compId: Int, tags: List<String>) {
        viewModelScope.launch {
            saveItem.invoke(
                ItemDetail(
                    id = 0,
                    name = itemName,
                    compId = compId,
                    tags = tags.map { ItemTag(id = 0, name = it, homeId = 0) }
                )
            ).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            itemId = result.data?.id
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

    private fun getItems(compartmentId: Int) {
        viewModelScope.launch {
            getCompartmentItems.invoke(compartmentId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                items = result.data ?: emptyList()
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

    private fun getCompartment(compartmentId: Int) {
        viewModelScope.launch {
            getCompartmentById.invoke(compartmentId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                compartment = result.data
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