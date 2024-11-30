package com.example.homeinventoryapp.data.remote.remoteDataSources

import com.example.homeinventoryapp.data.model.furniture.FurnitureService
import com.example.homeinventoryapp.data.model.toFurniture
import com.example.homeinventoryapp.data.model.toFurnitureRequestCreate
import com.example.homeinventoryapp.data.model.toFurnitureRequestUpdate
import com.example.homeinventoryapp.data.remote.BaseApiResponse
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class FurnitureRemoteDataSource @Inject constructor(
    private val furnitureService: FurnitureService,
) : BaseApiResponse() {

    suspend fun getFurniture(id: Int): NetworkResult<Furniture> {
        return try {
            val result = safeApiCall { furnitureService.getFurnitureById(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toFurniture()
                        ?.let { furniture -> NetworkResult.Success(furniture) }
                        ?: NetworkResult.Error(Constants.NO_FURNITURE_FOUND_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_FURNITURE_BY_ID_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_FURNITURE_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_FURNITURE_BY_ID_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }


    suspend fun getFurnitureByRoomId(id: Int): NetworkResult<List<Furniture>> {
        return try {
            val result = safeApiCall { furnitureService.getFurnitureByRoom(id) }
            when (result) {
                is NetworkResult.Success -> {
                    val mappedFurniture = result.data?.map { furnitureResponse ->
                        furnitureResponse.toFurniture()
                    }
                    NetworkResult.Success(mappedFurniture ?: emptyList())
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_FURNITURE_BY_ROOM_ID_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_FURNITURES_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_FURNITURE_BY_ROOM_ID_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun saveFurniture(furniture: Furniture): NetworkResult<Furniture> {
        return try {
            val result =
                safeApiCall { furnitureService.saveFurniture(furniture.toFurnitureRequestCreate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toFurniture()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.SAVING_FURNITURE_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.SAVING_FURNITURE_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.SAVING_FURNITURE_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.SAVING_FURNITURE_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun updateFurniture(furniture: Furniture): NetworkResult<Furniture> {
        return try {
            val result =
                safeApiCall { furnitureService.updateFurniture(furniture.toFurnitureRequestUpdate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toFurniture()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.UPDATING_FURNITURE_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.UPDATING_FURNITURE_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.UPDATING_FURNITURE_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.UPDATING_FURNITURE_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteFurniture(furnitureId: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { furnitureService.deleteFurniture(furnitureId) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.DELETING_FURNITURE_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.DELETING_FURNITURE_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.DELETING_FURNITURE_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}
