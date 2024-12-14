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
        val error: String? = null,
        val showEditDialogue: Boolean = false,
        val showInvitationDialogue: Boolean = false,
        val isUserOwner: Boolean? = false,
        val homeWasDeleted: Boolean = false,
        val invitationSentSuccessfully: Boolean = false
    )

    sealed class HomeEvent {
        class GetHome(val homeId: Int) : HomeEvent()
        class DeleteHome(val homeId: Int) : HomeEvent()
        class GetUserOwner(val homeId: Int, val userId: Int) : HomeEvent()
        class SendInvitation(val inviterId: Int, val inviteeUsername: String, val homeId: Int) :
            HomeEvent()

        data object ShowEditDialogue : HomeEvent()
        data object ClearEditDialogue : HomeEvent()
        data object ShowInvitationDialogue : HomeEvent()
        data object ClearInvitationDialogue : HomeEvent()
        class EditHome(val homeName: String, val homeId: Int) : HomeEvent()
        class GetHomeUsers(val homeId: Int) : HomeEvent()
        data object ErrorDisplayed : HomeEvent()
        data class UserClicked(val user: User) : HomeEvent()
        data object ClearUser : HomeEvent()
        data object ClearHomeWasDeleted : HomeEvent()
        data object ClearIsUserOwner : HomeEvent()
        data object ClearInvitationSentSuccessfully : HomeEvent()
    }

}