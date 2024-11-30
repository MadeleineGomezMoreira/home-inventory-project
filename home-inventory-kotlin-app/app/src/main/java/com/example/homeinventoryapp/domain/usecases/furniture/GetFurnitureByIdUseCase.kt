package com.example.homeinventoryapp.domain.usecases.furniture

import com.example.homeinventoryapp.data.repositories.FurnitureRepository
import javax.inject.Inject

class GetFurnitureByIdUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository
) {
    operator fun invoke(furnitureId: Int) = furnitureRepository.getFurniture(furnitureId)
}