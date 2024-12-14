package com.example.homeinventoryapp.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.invitation.AcceptInvitationUseCase
import com.example.homeinventoryapp.domain.usecases.invitation.DeclineInvitationUseCase
import com.example.homeinventoryapp.domain.usecases.invitation.GetInvitationInfoUseCase
import com.example.homeinventoryapp.domain.usecases.invitation.GetInvitationsByUserUseCase
import com.example.homeinventoryapp.domain.usecases.user.DeleteUserUseCase
import com.example.homeinventoryapp.domain.usecases.user.GetUserByIdUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getInvitationsByUser: GetInvitationsByUserUseCase,
    private val getUserById: GetUserByIdUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val takeUpInvitation: AcceptInvitationUseCase,
    private val declineInvitation: DeclineInvitationUseCase,
    private val getInvitationInfo: GetInvitationInfoUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<AccountContract.AccountState> by lazy {
        MutableStateFlow(AccountContract.AccountState())
    }

    val state: StateFlow<AccountContract.AccountState> = _state.asStateFlow()

    fun handleEvent(event: AccountContract.AccountEvent) {
        when (event) {
            is AccountContract.AccountEvent.AcceptInvitation -> acceptInvitation(event.invitationId)
            AccountContract.AccountEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is AccountContract.AccountEvent.GetInvitationInfo -> getInfo(event.invitationId)
            is AccountContract.AccountEvent.GetInvitations -> getInvitations(event.userId)
            is AccountContract.AccountEvent.GetUser -> getUser(event.userId)
            is AccountContract.AccountEvent.RejectInvitation -> rejectInvitation(event.invitationId)
            AccountContract.AccountEvent.ClearInvitationStatus -> _state.value =
                _state.value.copy(invitationStatusChanged = false)

            is AccountContract.AccountEvent.DeleteUser -> deleteUser(event.userId)
            AccountContract.AccountEvent.ClearUserWasDeleted -> _state.value =
                _state.value.copy(userWasDeleted = false)

            AccountContract.AccountEvent.ClearInvitationDialogue -> _state.value =
                _state.value.copy(showInvitationDialogue = false)

            AccountContract.AccountEvent.ShowInvitationDialogue -> _state.value =
                _state.value.copy(showInvitationDialogue = true)
        }
    }

    private fun deleteUser(userId: Int) {
        viewModelScope.launch {
            deleteUserUseCase.invoke(userId)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                userWasDeleted = true
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                error = result.message,
                                isLoading = false
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


    private fun getUser(userId: Int) {
        viewModelScope.launch {
            getUserById.invoke(userId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            user = result.data, isLoading = false
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message, isLoading = false
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

    private fun getInvitations(userId: Int) {
        viewModelScope.launch {
            getInvitationsByUser.invoke(userId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            invitations = result.data, isLoading = false
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message, isLoading = false
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

    private fun acceptInvitation(invitationId: Int) {
        viewModelScope.launch {
            takeUpInvitation.invoke(invitationId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            invitationStatusChanged = true, isLoading = false
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message, isLoading = false
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

    private fun rejectInvitation(invitationId: Int) {
        viewModelScope.launch {
            declineInvitation.invoke(invitationId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            invitationStatusChanged = true, isLoading = false
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message, isLoading = false
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

    private fun getInfo(invitationId: Int) {
        viewModelScope.launch {
            getInvitationInfo.invoke(invitationId).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            invitationInfo = result.data, isLoading = false
                        )
                    }

                    is NetworkResult.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message, isLoading = false
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
}