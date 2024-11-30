package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.remoteDataSources.RoomRemoteDataSource
import com.example.homeinventoryapp.domain.model.Room
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomRemoteDataSource: RoomRemoteDataSource
) {

    fun getRoom(id: Int): Flow<NetworkResult<Room>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = roomRemoteDataSource.getRoomById(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getRoomsByHome(id: Int): Flow<NetworkResult<List<Room>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = roomRemoteDataSource.getRoomsByHome(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun saveRoom(room: Room): Flow<NetworkResult<Room>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = roomRemoteDataSource.saveRoom(room)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun updateRoom(room: Room): Flow<NetworkResult<Room>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = roomRemoteDataSource.updateRoom(room)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteRoom(roomId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = roomRemoteDataSource.deleteRoom(roomId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}