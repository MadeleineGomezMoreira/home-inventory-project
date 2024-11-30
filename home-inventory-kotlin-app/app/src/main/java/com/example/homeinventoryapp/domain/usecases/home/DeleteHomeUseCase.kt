package com.example.homeinventoryapp.domain.usecases.home

import com.example.homeinventoryapp.data.repositories.HomeRepository
import javax.inject.Inject

class DeleteHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    operator fun invoke(homeId: Int) = homeRepository.deleteHome(homeId)

}