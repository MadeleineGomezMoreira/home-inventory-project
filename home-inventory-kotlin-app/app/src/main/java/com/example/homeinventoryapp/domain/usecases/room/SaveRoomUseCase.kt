package com.example.homeinventoryapp.domain.usecases.room

import com.example.homeinventoryapp.data.repositories.RoomRepository
import com.example.homeinventoryapp.domain.model.Room
import javax.inject.Inject

class SaveRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    operator fun invoke(roomName: String, homeId: Int) = roomRepository.saveRoom(
        Room(
            id = 0,
            name = roomName,
            homeId = homeId
        )
    )

}