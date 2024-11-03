package com.example.homeinventoryapp.domain.usecases.home

import com.example.homeinventoryapp.data.repositories.HomeRepository
import javax.inject.Inject

class GetUserHomesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(userId : Int) = homeRepository.getHomesByUser(userId)
}