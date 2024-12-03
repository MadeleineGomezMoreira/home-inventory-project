package com.example.homeinventoryapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homeinventoryapp.domain.usecases.user.RegisterUseCase
import com.example.homeinventoryapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<RegisterContract.RegisterState> by lazy {
        MutableStateFlow(RegisterContract.RegisterState())
    }

    val state: StateFlow<RegisterContract.RegisterState> = _state.asStateFlow()

    fun handleEvent(event: RegisterContract.RegisterEvent) {
        when (event) {
            is RegisterContract.RegisterEvent.EmailChanged -> _state.value =
                _state.value.copy(email = event.email)

            RegisterContract.RegisterEvent.ErrorDisplayed -> _state.value =
                _state.value.copy(error = null)

            is RegisterContract.RegisterEvent.PasswordChanged -> _state.value =
                _state.value.copy(password = event.password)

            is RegisterContract.RegisterEvent.Register -> register()
            is RegisterContract.RegisterEvent.UsernameChanged -> _state.value =
                _state.value.copy(username = event.username)

            RegisterContract.RegisterEvent.ClearRegisterSuccessful -> _state.value =
                _state.value.copy(isRegisterSuccessful = false)
        }
    }

    private fun register() {
        viewModelScope.launch {
            registerUseCase.invoke(_state.value.username, _state.value.password, _state.value.email)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                isRegisterSuccessful = true
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