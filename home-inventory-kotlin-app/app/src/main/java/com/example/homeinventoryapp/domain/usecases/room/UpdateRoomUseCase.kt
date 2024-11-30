package com.example.homeinventoryapp.domain.usecases.room

import com.example.homeinventoryapp.data.repositories.RoomRepository
import com.example.homeinventoryapp.domain.model.Room
import javax.inject.Inject

class UpdateRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    operator fun invoke(roomId: Int, roomName: String) = roomRepository.updateRoom(
        Room(
            id = roomId,
            name = roomName,
            homeId = 0
        )
    )


}