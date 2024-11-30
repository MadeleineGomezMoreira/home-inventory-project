package com.example.homeinventoryapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.home.GetHomeUseCase
import com.example.homeinventoryapp.domain.usecases.user.GetHomeUsersUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHome: GetHomeUseCase,
    private val getHomeUsers: GetHomeUsersUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeContract.HomeState> by lazy {
        MutableStateFlow(HomeContract.HomeState())
    }

    val state: StateFlow<HomeContract.HomeState> = _state.asStateFlow()

    fun handleEvent(event: HomeContract.HomeEvent) {
        when (event) {
            is HomeContract.HomeEvent.GetHome -> retrieveHome(event.homeId)
            is HomeContract.HomeEvent.GetHomeUsers -> retrieveHomeUsers(event.homeId)
            is HomeContract.HomeEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is HomeContract.HomeEvent.UserClicked -> _state.value =
                _state.value.copy(userId = event.user.id)

            is HomeContract.HomeEvent.ClearUser -> _state.value =
                _state.value.copy(userId = null)
        }
    }

    private fun retrieveHome(homeId: Int) {
        viewModelScope.launch {
            getHome.invoke(homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                home = result.data
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

    private fun retrieveHomeUsers(homeId: Int) {
        viewModelScope.launch {
            getHomeUsers.invoke(homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                owner = result.data?.owner,
                                members = result.data?.members ?: emptyList()
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