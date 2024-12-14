package com.example.homeinventoryapp.domain.usecases.invitation

import com.example.homeinventoryapp.data.repositories.InvitationRepository
import javax.inject.Inject

class DeclineInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    operator fun invoke(invitationId: Int) = invitationRepository.declineInvitation(invitationId)
}