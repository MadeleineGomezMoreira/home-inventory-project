package com.example.homeinventoryapp.domain.usecases.user

import com.example.homeinventoryapp.data.repositories.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(id: Int) = userRepository.getUserById(id)
}