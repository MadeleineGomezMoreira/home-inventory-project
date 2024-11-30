package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.remoteDataSources.CompartmentRemoteDataSource
import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CompartmentRepository @Inject constructor(
    private val compartmentRemoteDataSource: CompartmentRemoteDataSource
) {

    fun getCompartment(id: Int): Flow<NetworkResult<Compartment>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = compartmentRemoteDataSource.getCompartmentById(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getCompartmentsByFurniture(id: Int): Flow<NetworkResult<List<Compartment>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = compartmentRemoteDataSource.getCompartmentsByFurniture(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun saveCompartment(compartment: Compartment): Flow<NetworkResult<Compartment>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = compartmentRemoteDataSource.saveCompartment(compartment)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun updateCompartment(compartment: Compartment): Flow<NetworkResult<Compartment>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = compartmentRemoteDataSource.updateCompartment(compartment)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun deleteCompartment(compartmentId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = compartmentRemoteDataSource.deleteCompartment(compartmentId)
            emit(result)
        }
    }

}