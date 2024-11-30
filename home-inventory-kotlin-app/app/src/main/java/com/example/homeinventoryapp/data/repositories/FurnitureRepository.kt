package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.remoteDataSources.FurnitureRemoteDataSource
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FurnitureRepository @Inject constructor(
    private val furnitureRemoteDataSource: FurnitureRemoteDataSource
) {

    fun getFurnitureByRoom(id: Int): Flow<NetworkResult<List<Furniture>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = furnitureRemoteDataSource.getFurnitureByRoomId(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getFurniture(id: Int): Flow<NetworkResult<Furniture>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = furnitureRemoteDataSource.getFurniture(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun saveFurniture(furniture: Furniture): Flow<NetworkResult<Furniture>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = furnitureRemoteDataSource.saveFurniture(furniture)
            emit(result)
        }
    }

    fun updateFurniture(furniture: Furniture): Flow<NetworkResult<Furniture>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = furnitureRemoteDataSource.updateFurniture(furniture)
            emit(result)
        }
    }

    fun deleteFurniture(furnitureId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = furnitureRemoteDataSource.deleteFurniture(furnitureId)
            emit(result)
        }
    }

}