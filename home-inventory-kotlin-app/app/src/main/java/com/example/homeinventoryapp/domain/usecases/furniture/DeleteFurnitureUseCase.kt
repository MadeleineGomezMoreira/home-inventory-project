package com.example.homeinventoryapp.domain.usecases.furniture

import com.example.homeinventoryapp.data.repositories.FurnitureRepository
import javax.inject.Inject

class DeleteFurnitureUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository
) {

    operator fun invoke(id: Int) = furnitureRepository.deleteFurniture(id)

}