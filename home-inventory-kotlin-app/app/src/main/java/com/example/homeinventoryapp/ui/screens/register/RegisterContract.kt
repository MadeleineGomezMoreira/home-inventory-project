package com.example.homeinventoryapp.ui.screens.register

class RegisterContract {

    data class RegisterState(
        val username: String = "",
        val password: String = "",
        val email: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val isRegisterSuccessful: Boolean = false
    )

    sealed class RegisterEvent {
        data object Register : RegisterEvent()
        data object ErrorDisplayed : RegisterEvent()
        data object ClearRegisterSuccessful : RegisterEvent()
        data class UsernameChanged(val username: String) : RegisterEvent()
        data class PasswordChanged(val password: String) : RegisterEvent()
        data class EmailChanged(val email: String) : RegisterEvent()
    }
}