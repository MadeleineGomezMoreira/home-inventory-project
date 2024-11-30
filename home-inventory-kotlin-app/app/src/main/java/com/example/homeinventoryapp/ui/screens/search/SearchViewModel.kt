package com.example.homeinventoryapp.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.item.GetItemsBySearchWordUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getItemBySearchWord: GetItemsBySearchWordUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<SearchContract.SearchState> by lazy {
        MutableStateFlow(SearchContract.SearchState())
    }

    fun handleEvent(event: SearchContract.SearchEvent) {
        when (event) {
            is SearchContract.SearchEvent.GetSearch -> searchItems(event.homeId, event.searchWord)
            is SearchContract.SearchEvent.ItemClicked -> _state.value =
                _state.value.copy(itemId = event.item.id)

            is SearchContract.SearchEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is SearchContract.SearchEvent.ClearItem -> _state.value =
                _state.value.copy(itemId = null)

            is SearchContract.SearchEvent.UpdateSearchWord -> _state.value =
                _state.value.copy(searchWord = event.searchWord)
        }
    }

    val state: StateFlow<SearchContract.SearchState> = _state.asStateFlow()


    private fun searchItems(homeId: Int, searchWord: String) {
        viewModelScope.launch {
            getItemBySearchWord(homeId, searchWord)
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


}