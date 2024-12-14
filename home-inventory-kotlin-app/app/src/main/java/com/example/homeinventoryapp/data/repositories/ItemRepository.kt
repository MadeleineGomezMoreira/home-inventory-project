package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.model.item.ItemMoveRequest
import com.example.homeinventoryapp.data.remote.remoteDataSources.ItemRemoteDataSource
import com.example.homeinventoryapp.domain.model.Item
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemRemoteDataSource: ItemRemoteDataSource
) {

    fun moveItem(item: ItemMoveRequest): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.moveItem(item)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getItemsByCompartment(id: Int): Flow<NetworkResult<List<Item>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.getItemsByCompartment(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getItemById(id: Int): Flow<NetworkResult<ItemDetail>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.getItem(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getItemsBySearchWord(homeId: Int, searchWord: String): Flow<NetworkResult<List<Item>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.getItemsBySearchWord(homeId, searchWord)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun saveItem(item: ItemDetail): Flow<NetworkResult<ItemDetail>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.saveItem(item)
            emit(result)
        }
    }

    fun getItemRoute(id: Int): Flow<NetworkResult<String>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.getItemRoute(id)
            emit(result)
        }
    }

    fun updateItem(item: ItemDetail): Flow<NetworkResult<ItemDetail>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.updateItem(item)
            emit(result)
        }
    }

    fun deleteItem(itemId: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = itemRemoteDataSource.deleteItem(itemId)
            emit(result)
        }
    }

}