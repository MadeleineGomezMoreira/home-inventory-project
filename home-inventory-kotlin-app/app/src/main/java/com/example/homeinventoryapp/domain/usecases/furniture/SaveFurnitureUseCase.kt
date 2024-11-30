package com.example.homeinventoryapp.domain.usecases.furniture

import com.example.homeinventoryapp.data.repositories.FurnitureRepository
import com.example.homeinventoryapp.domain.model.Furniture
import javax.inject.Inject

class SaveFurnitureUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository
) {

    operator fun invoke(furnitureName: String, roomId: Int) = furnitureRepository.saveFurniture(
        Furniture(
            id = 0,
            name = furnitureName,
            roomId = roomId
        )
    )

}