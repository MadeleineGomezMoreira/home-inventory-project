package com.example.homeinventoryapp.domain.usecases.room

import com.example.homeinventoryapp.data.repositories.RoomRepository
import javax.inject.Inject

class GetRoomsByHomeUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    operator fun invoke(homeId: Int) = roomRepository.getRoomsByHome(homeId)
}