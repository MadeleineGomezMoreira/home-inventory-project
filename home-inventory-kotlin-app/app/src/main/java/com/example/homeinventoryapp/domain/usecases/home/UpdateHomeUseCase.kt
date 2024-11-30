package com.example.homeinventoryapp.domain.usecases.home

import com.example.homeinventoryapp.data.repositories.HomeRepository
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.utils.Constants
import javax.inject.Inject

class UpdateHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    operator fun invoke(homeId: Int, homeName: String) = homeRepository.updateHome(
        Home(
            id = homeId,
            name = homeName,
            owner = Constants.ZERO_CODE
        )
    )

}