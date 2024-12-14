package com.example.homeinventoryapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.home.DeleteHomeUseCase
import com.example.homeinventoryapp.domain.usecases.home.GetHomeOwnershipUseCase
import com.example.homeinventoryapp.domain.usecases.home.GetHomeUseCase
import com.example.homeinventoryapp.domain.usecases.home.UpdateHomeUseCase
import com.example.homeinventoryapp.domain.usecases.invitation.SendInvitationUseCase
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
    private val updateHome: UpdateHomeUseCase,
    private val getHomeUsers: GetHomeUsersUseCase,
    private val getOwnership: GetHomeOwnershipUseCase,
    private val deleteSingleHome: DeleteHomeUseCase,
    private val sendInvitationToUser: SendInvitationUseCase,
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

            HomeContract.HomeEvent.ClearEditDialogue -> _state.value =
                _state.value.copy(showEditDialogue = false)

            is HomeContract.HomeEvent.EditHome -> editHome(event.homeName, event.homeId)
            is HomeContract.HomeEvent.ShowEditDialogue -> _state.value =
                _state.value.copy(showEditDialogue = true)

            HomeContract.HomeEvent.ClearIsUserOwner -> _state.value =
                _state.value.copy(isUserOwner = false)

            is HomeContract.HomeEvent.GetUserOwner -> getUserHomeOwnership(
                event.homeId,
                event.userId
            )

            HomeContract.HomeEvent.ClearHomeWasDeleted -> _state.value =
                _state.value.copy(homeWasDeleted = false)

            is HomeContract.HomeEvent.DeleteHome -> deleteHome(event.homeId)
            HomeContract.HomeEvent.ClearInvitationDialogue -> _state.value =
                _state.value.copy(showInvitationDialogue = false)

            HomeContract.HomeEvent.ShowInvitationDialogue -> _state.value =
                _state.value.copy(showInvitationDialogue = true)

            is HomeContract.HomeEvent.SendInvitation -> sendInvitation(
                event.inviterId,
                event.inviteeUsername,
                event.homeId
            )

            is HomeContract.HomeEvent.ClearInvitationSentSuccessfully -> _state.value =
                _state.value.copy(invitationSentSuccessfully = false)
        }
    }

    private fun sendInvitation(inviterId: Int, inviteeUsername: String, homeId: Int) {
        viewModelScope.launch {
            sendInvitationToUser.invoke(inviterId, inviteeUsername, homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                invitationSentSuccessfully = true,
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }

                        is NetworkResult.Loading -> {
                            _state.value = _state.value.copy(
                                isLoading = true
                            )

                        }
                    }
                }
        }
    }

    private fun deleteHome(homeId: Int) {
        viewModelScope.launch {
            deleteSingleHome.invoke(homeId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                homeWasDeleted = true
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

    private fun getUserHomeOwnership(homeId: Int, userId: Int) {
        viewModelScope.launch {
            getOwnership.invoke(homeId, userId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                isUserOwner = result.data
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

    private fun editHome(homeName: String, homeId: Int) {
        viewModelScope.launch {
            updateHome.invoke(homeId, homeName)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                home = result.data,
                                isLoading = false,
                                showEditDialogue = false
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