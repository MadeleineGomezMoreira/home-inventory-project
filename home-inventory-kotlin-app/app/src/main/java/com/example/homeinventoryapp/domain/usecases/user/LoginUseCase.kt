package com.example.homeinventoryapp.domain.usecases.user

import com.example.homeinventoryapp.data.repositories.UserRepository
import com.example.homeinventoryapp.domain.model.LoginDTO
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(username: String, password: String) =
        userRepository.loginUser(LoginDTO(username, password))
}