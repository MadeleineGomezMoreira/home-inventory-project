package com.example.homeinventoryapp.data.remote.remoteDataSources

import com.example.homeinventoryapp.data.model.toRoom
import com.example.homeinventoryapp.data.model.toRoomRequestCreate
import com.example.homeinventoryapp.data.model.toRoomRequestUpdate
import com.example.homeinventoryapp.data.remote.BaseApiResponse
import com.example.homeinventoryapp.data.remote.services.RoomService
import com.example.homeinventoryapp.domain.model.Room
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class RoomRemoteDataSource @Inject constructor(
    private val roomService: RoomService
) : BaseApiResponse() {

    suspend fun getRoomsByHome(id: Int): NetworkResult<List<Room>> {
        return try {
            val result = safeApiCall { roomService.getRoomsByHome(id) }
            when (result) {
                is NetworkResult.Success -> {
                    val mappedRooms = result.data?.map { roomResponse ->
                        roomResponse.toRoom()
                    }
                    NetworkResult.Success(mappedRooms ?: emptyList())
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_ROOMS_BY_HOME_ID_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_ROOMS_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_ROOMS_BY_HOME_ID_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getRoomById(id: Int): NetworkResult<Room> {
        return try {
            val result = safeApiCall { roomService.getRoom(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toRoom()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.RETRIEVING_ROOM_BY_ID_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_ROOM_BY_ID_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_ROOM_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_ROOM_BY_ID_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun saveRoom(room: Room): NetworkResult<Room> {
        return try {
            val result = safeApiCall { roomService.saveRoom(room.toRoomRequestCreate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toRoom()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.SAVING_ROOM_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.SAVING_ROOM_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.SAVING_ROOM_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.SAVING_ROOM_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun updateRoom(room: Room): NetworkResult<Room> {
        return try {
            val result = safeApiCall { roomService.updateRoom(room.toRoomRequestUpdate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toRoom()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.UPDATING_ROOM_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.UPDATING_ROOM_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.UPDATING_ROOM_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.UPDATING_ROOM_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteRoom(roomId: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { roomService.deleteRoom(roomId) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.DELETING_ROOM_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.DELETING_ROOM_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.DELETING_ROOM_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

}