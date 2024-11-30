package com.example.homeinventoryapp.ui.screens.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.domain.usecases.item.GetItemByIdUseCase
import com.example.homeinventoryapp.domain.usecases.item.GetItemRouteUseCase
import com.example.homeinventoryapp.domain.usecases.item.UpdateItemUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val getItemDetailById: GetItemByIdUseCase,
    private val updateItemDetail: UpdateItemUseCase,
    private val getItemRoute: GetItemRouteUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<ItemContract.ItemState> by lazy {
        MutableStateFlow(ItemContract.ItemState())
    }

    val state: StateFlow<ItemContract.ItemState> = _state.asStateFlow()

    fun handleEvent(event: ItemContract.ItemEvent) {
        when (event) {
            is ItemContract.ItemEvent.GetItem -> getItem(event.itemId)
            is ItemContract.ItemEvent.GetItemRoute -> getItemRoute(event.itemId)
            is ItemContract.ItemEvent.UpdateItem -> updateItem(event.item)
            ItemContract.ItemEvent.ShowDialogue -> _state.value =
                _state.value.copy(showEditDialogue = true)

            ItemContract.ItemEvent.ClearDialogue -> _state.value =
                _state.value.copy(showEditDialogue = false)

            ItemContract.ItemEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)
        }
    }

    private fun getItemRoute(itemId: Int) {
        viewModelScope.launch {
            getItemRoute.invoke(itemId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                itemRoute = result.data
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

    private fun updateItem(item: ItemDetail) {
        viewModelScope.launch {
            updateItemDetail.invoke(item)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                item = result.data
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

    private fun getItem(itemId: Int) {
        viewModelScope.launch {
            getItemDetailById.invoke(itemId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                item = result.data
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