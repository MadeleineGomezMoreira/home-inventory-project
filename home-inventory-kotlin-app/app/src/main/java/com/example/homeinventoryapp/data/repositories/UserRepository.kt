package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.remoteDataSources.UserRemoteDataSource
import com.example.homeinventoryapp.domain.model.HomeUsers
import com.example.homeinventoryapp.domain.model.LoginDTO
import com.example.homeinventoryapp.domain.model.RegisterDTO
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

    fun getUsersByHomeId(id: Int): Flow<NetworkResult<HomeUsers>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = userRemoteDataSource.getUsersByHomeId(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun registerUser(user: RegisterDTO): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = userRemoteDataSource.registerUser(user)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun loginUser(user: LoginDTO): Flow<NetworkResult<Int>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = userRemoteDataSource.loginUser(user)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteUser(userId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = userRemoteDataSource.deleteUser(userId)
            emit(result)
        }
    }

}