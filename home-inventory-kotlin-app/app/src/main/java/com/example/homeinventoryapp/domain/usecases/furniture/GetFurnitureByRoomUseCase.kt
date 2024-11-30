package com.example.homeinventoryapp.domain.usecases.furniture

import com.example.homeinventoryapp.data.repositories.FurnitureRepository
import javax.inject.Inject

class GetFurnitureByRoomUseCase @Inject constructor(
    private val furnitureRepository: FurnitureRepository
) {

    operator fun invoke(roomId: Int) = furnitureRepository.getFurnitureByRoom(roomId)

}