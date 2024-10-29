package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.UserRemoteDataSource
import com.example.homeinventoryapp.domain.model.User
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {

    fun getUserByUsername(username: String): Flow<NetworkResult<User>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = userRemoteDataSource.getUserByUsername(username)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getUserById(id: Int): Flow<NetworkResult<User>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = userRemoteDataSource.getUserById(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

}