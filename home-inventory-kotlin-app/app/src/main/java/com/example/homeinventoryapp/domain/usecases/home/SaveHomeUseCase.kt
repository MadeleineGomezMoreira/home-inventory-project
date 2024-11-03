package com.example.homeinventoryapp.domain.usecases.home

import com.example.homeinventoryapp.data.repositories.HomeRepository
import com.example.homeinventoryapp.domain.model.Home
import javax.inject.Inject

class SaveHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(homeName: String, userId: Int) = homeRepository.saveHome(
        Home(
            id = 0,
            name = homeName,
            owner = userId
        )
    )
}