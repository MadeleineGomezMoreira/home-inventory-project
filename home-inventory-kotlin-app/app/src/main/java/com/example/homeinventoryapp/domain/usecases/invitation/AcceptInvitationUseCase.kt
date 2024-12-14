package com.example.homeinventoryapp.domain.usecases.invitation

import com.example.homeinventoryapp.data.repositories.InvitationRepository
import javax.inject.Inject

class AcceptInvitationUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    operator fun invoke(invitationId: Int) = invitationRepository.acceptInvitation(invitationId)
}