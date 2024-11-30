package com.example.homeinventoryapp.domain.usecases.furniture

import com.example.homeinventoryapp.data.repositories.FurnitureRepository
import com.example.homeinventoryapp.domain.model.Furniture
import javax.inject.Inject

class UpdateFurnitureUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository
) {

    operator fun invoke(furnitureId: Int, furnitureName: String) =
        furnitureRepository.updateFurniture(
            Furniture(
                id = furnitureId,
                name = furnitureName,
                roomId = 0
            )
        )

}