package com.example.homeinventoryapp.ui.screens.home

import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.User

class HomeContract {

    data class HomeState(
        val home: Home? = null,
        val owner: User? = null,
        val members: List<User> = emptyList(),
        val userId: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class HomeEvent {
        class GetHome(val homeId: Int) : HomeEvent()
        class GetHomeUsers(val homeId: Int) : HomeEvent()
        data object ErrorDisplayed : HomeEvent()
        data class UserClicked(val user: User) : HomeEvent()
        data object ClearUser : HomeEvent()
    }

}