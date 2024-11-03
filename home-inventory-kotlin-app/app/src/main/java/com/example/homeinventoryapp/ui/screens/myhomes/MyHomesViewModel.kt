package com.example.homeinventoryapp.ui.screens.myhomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.home.GetUserHomesUseCase
import com.example.homeinventoryapp.domain.usecases.home.SaveHomeUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyHomesViewModel @Inject constructor(
    private val getUserHomes: GetUserHomesUseCase,
    private val saveHome: SaveHomeUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<MyHomesContract.MyHomesState> by lazy {
        MutableStateFlow(MyHomesContract.MyHomesState())
    }

    val state: StateFlow<MyHomesContract.MyHomesState> = _state.asStateFlow()

    fun handleEvent(event: MyHomesContract.MyHomesEvent) {
        when (event) {
            is MyHomesContract.MyHomesEvent.CreateHome -> createHome(event.homeName, event.userId)
            is MyHomesContract.MyHomesEvent.GetHomes -> getMyHomes(event.userId)
            is MyHomesContract.MyHomesEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is MyHomesContract.MyHomesEvent.HomeClicked -> _state.value =
                _state.value.copy(homeId = event.home.id)

            is MyHomesContract.MyHomesEvent.ClearHome -> _state.value =
                _state.value.copy(homeId = null)

            is MyHomesContract.MyHomesEvent.ClearDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = false)

            is MyHomesContract.MyHomesEvent.ShowDialogue -> _state.value =
                _state.value.copy(showCreateDialogue = true)
        }
    }

    private fun createHome(homeName: String, userId: Int?) {
        viewModelScope.launch {
            saveHome.invoke(homeName, userId ?: 0)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                homeId = result.data?.id,
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

    private fun getMyHomes(userId: Int) {
        viewModelScope.launch {
            getUserHomes.invoke(userId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                ownerHomes = result.data?.ownedHomes ?: emptyList(),
                                memberHomes = result.data?.memberHomes ?: emptyList(),
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
