package com.example.homeinventoryapp.ui.screens.account

import com.example.homeinventoryapp.domain.model.Invitation
import com.example.homeinventoryapp.domain.model.InvitationInfo
import com.example.homeinventoryapp.domain.model.User

class AccountContract {

    data class AccountState(
        val user: User? = null,
        val invitationInfo: InvitationInfo? = null,
        val invitationStatusChanged: Boolean = false,
        val userWasDeleted: Boolean = false,
        val invitations: List<Invitation>? = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val showInvitationDialogue: Boolean = false
    )

    //invitations would be accepted or rejected in the dialog
    sealed class AccountEvent {
        data class GetUser(val userId: Int) : AccountEvent()
        data class GetInvitations(val userId: Int) : AccountEvent()
        data class GetInvitationInfo(val invitationId: Int) : AccountEvent()
        data class RejectInvitation(val invitationId: Int) : AccountEvent()
        data class AcceptInvitation(val invitationId: Int) : AccountEvent()
        data class DeleteUser(val userId: Int) : AccountEvent()
        data object ClearUserWasDeleted : AccountEvent()
        data object ShowInvitationDialogue : AccountEvent()
        data object ClearInvitationDialogue : AccountEvent()
        data object ErrorDisplayed : AccountEvent()
        data object ClearInvitationStatus : AccountEvent()
    }

}