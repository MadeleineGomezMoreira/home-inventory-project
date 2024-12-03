package com.example.homeinventoryapp.ui.screens.login

class LoginContract {

    data class LoginState(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val userId: Int? = null
    )

    sealed class LoginEvent {
        data object Login : LoginEvent()
        data object ErrorDisplayed : LoginEvent()
        data object ClearUserId : LoginEvent()
        data class UsernameChanged(val username: String) : LoginEvent()
        data class PasswordChanged(val password: String) : LoginEvent()
    }
}