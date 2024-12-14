package com.example.homeinventoryapp.domain.usecases.invitation

import com.example.homeinventoryapp.data.repositories.InvitationRepository
import javax.inject.Inject

class GetInvitationsByUserUseCase @Inject constructor(
    private val invitationRepository: InvitationRepository
) {
    operator fun invoke(userId: Int) = invitationRepository.getInvitationsByUser(userId)
}