package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.remoteDataSources.HomeRemoteDataSource
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.MyHomes
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource,
) {

    fun getHome(id: Int): Flow<NetworkResult<Home>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = homeRemoteDataSource.getHome(id)
            emit(result)
        }
    }

    fun getHomesByUser(id: Int): Flow<NetworkResult<MyHomes>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = homeRemoteDataSource.getHomesByUser(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun saveHome(home: Home): Flow<NetworkResult<Home>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = homeRemoteDataSource.saveHome(home)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteHome(id: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = homeRemoteDataSource.deleteHome(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}