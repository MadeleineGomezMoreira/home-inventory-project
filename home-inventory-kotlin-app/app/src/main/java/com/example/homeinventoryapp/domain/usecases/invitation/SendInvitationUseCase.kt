package com.example.homeinventoryapp.domain.usecases.invitation

import com.example.homeinventoryapp.data.repositories.InvitationRepository
import com.example.homeinventoryapp.domain.model.InvitationToSend
import javax.inject.Inject

class SendInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    operator fun invoke(inviterId: Int, inviteeUsername: String, homeId: Int) =
        invitationRepository.sendInvitation(InvitationToSend(inviterId, inviteeUsername, homeId))
}