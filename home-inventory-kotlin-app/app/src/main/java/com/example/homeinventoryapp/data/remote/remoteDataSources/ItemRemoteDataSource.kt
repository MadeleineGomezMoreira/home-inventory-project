package com.example.homeinventoryapp.data.remote.remoteDataSources

import com.example.homeinventoryapp.data.model.toItem
import com.example.homeinventoryapp.data.model.toItemDetail
import com.example.homeinventoryapp.data.model.toItemRequestCreate
import com.example.homeinventoryapp.data.model.toItemRequestUpdate
import com.example.homeinventoryapp.data.remote.BaseApiResponse
import com.example.homeinventoryapp.data.remote.services.ItemService
import com.example.homeinventoryapp.domain.model.Item
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class ItemRemoteDataSource @Inject constructor(
    private val itemService: ItemService,
) : BaseApiResponse() {

    suspend fun getItemRoute(id: Int): NetworkResult<String> {
        return try {
            val result = safeApiCall { itemService.getItemRoute(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.RETRIEVING_ITEM_ROUTE_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_ITEM_ROUTE_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.RETRIEVING_ITEM_ROUTE_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_ITEMS_BY_COMPARTMENT_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getItemsByCompartment(id: Int): NetworkResult<List<Item>> {
        return try {
            val result = safeApiCall { itemService.getItemsByCompartment(id) }
            when (result) {
                is NetworkResult.Success -> {
                    val mappedItems = result.data?.map { it.toItem() } ?: emptyList()
                    NetworkResult.Success(mappedItems)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_ITEMS_BY_COMPARTMENT_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_ITEMS_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_ITEMS_BY_COMPARTMENT_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getItemsBySearchWord(homeId: Int, searchWord: String): NetworkResult<List<Item>> {
        return try {
            val result = safeApiCall { itemService.getItemsByString(homeId, searchWord) }
            when (result) {
                is NetworkResult.Success -> {
                    val mappedItems = result.data?.map { it.toItem() } ?: emptyList()
                    NetworkResult.Success(mappedItems)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_ITEMS_BY_SEARCH_WORD_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_ITEMS_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_ITEMS_BY_SEARCH_WORD_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getItem(id: Int): NetworkResult<ItemDetail> {
        return try {
            val result = safeApiCall { itemService.getItemById(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toItemDetail()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.RETRIEVING_ITEM_BY_ID_ERROR)

                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_ITEM_BY_ID_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_ITEMS_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_ITEM_BY_ID_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun saveItem(item: ItemDetail): NetworkResult<ItemDetail> {
        return try {
            val result = safeApiCall { itemService.saveItem(item.toItemRequestCreate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toItemDetail()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.SAVING_ROOM_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.SAVING_ITEM_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.SAVING_ITEM_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }

            }
        } catch (e: Exception) {
            Timber.e(e, Constants.SAVING_ITEM_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun updateItem(item: ItemDetail): NetworkResult<ItemDetail> {
        return try {
            val result = safeApiCall { itemService.updateItem(item.toItemRequestUpdate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toItemDetail()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.UPDATING_ITEM_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.UPDATING_ITEM_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.UPDATING_ITEM_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.UPDATING_ITEM_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteItem(id: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { itemService.deleteItem(id) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.DELETING_ITEM_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.DELETING_ITEM_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.DELETING_ITEM_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}