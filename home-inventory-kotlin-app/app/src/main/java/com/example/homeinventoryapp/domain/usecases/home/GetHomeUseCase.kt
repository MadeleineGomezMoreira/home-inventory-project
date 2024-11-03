package com.example.homeinventoryapp.domain.usecases.home

import com.example.homeinventoryapp.data.repositories.HomeRepository
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(id: Int) = homeRepository.getHome(id)
}