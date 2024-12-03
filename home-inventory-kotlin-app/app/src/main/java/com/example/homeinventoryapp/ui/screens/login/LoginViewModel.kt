package com.example.homeinventoryapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.user.LoginUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUser: LoginUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<LoginContract.LoginState> by lazy {
        MutableStateFlow(LoginContract.LoginState())
    }

    val state: StateFlow<LoginContract.LoginState> = _state.asStateFlow()

    fun handleEvent(event: LoginContract.LoginEvent) {
        when (event) {
            LoginContract.LoginEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is LoginContract.LoginEvent.Login -> login()
            is LoginContract.LoginEvent.PasswordChanged -> _state.value =
                _state.value.copy(password = event.password)

            is LoginContract.LoginEvent.UsernameChanged -> _state.value =
                _state.value.copy(username = event.username)

            LoginContract.LoginEvent.ClearUserId -> _state.value = _state.value.copy(userId = null)
        }
    }

    private fun login() {
        viewModelScope.launch {
            loginUser.invoke(_state.value.username, _state.value.password)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                userId = result.data,
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
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

}
