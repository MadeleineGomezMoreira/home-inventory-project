package com.example.homeinventoryapp.domain.usecases.user

import com.example.homeinventoryapp.data.repositories.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int) = userRepository.deleteUser(userId)
}