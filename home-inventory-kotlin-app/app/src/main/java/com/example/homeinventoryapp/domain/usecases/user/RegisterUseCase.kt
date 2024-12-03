package com.example.homeinventoryapp.domain.usecases.user

import com.example.homeinventoryapp.data.repositories.UserRepository
import com.example.homeinventoryapp.domain.model.RegisterDTO
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String, password: String, email: String) =
        userRepository.registerUser(RegisterDTO(username, password, email))
}