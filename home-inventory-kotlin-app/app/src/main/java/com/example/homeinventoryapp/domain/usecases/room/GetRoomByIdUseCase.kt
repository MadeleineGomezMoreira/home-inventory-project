package com.example.homeinventoryapp.domain.usecases.room

import com.example.homeinventoryapp.data.repositories.RoomRepository
import javax.inject.Inject

class GetRoomByIdUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    operator fun invoke(roomId: Int) = roomRepository.getRoom(roomId)

}