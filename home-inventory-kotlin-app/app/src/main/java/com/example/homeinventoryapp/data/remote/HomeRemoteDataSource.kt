package com.example.homeinventoryapp.data.remote

import com.example.homeinventoryapp.data.model.toHome
import com.example.homeinventoryapp.data.model.toHomeResponse
import com.example.homeinventoryapp.data.remote.services.HomeService
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val homeService: HomeService
) : BaseApiResponse() {

    suspend fun getHomesByUser(id: Int): NetworkResult<List<Home>> {
        return try {
            val result = safeApiCall { homeService.getHomesByUser(id) }
            when (result) {
                is NetworkResult.Success -> {
                    val homes = result.data?.map { homeResponse ->
                        homeResponse.toHome()
                    } ?: emptyList()
                    if (homes.isEmpty()) return NetworkResult.Error(Constants.NO_HOMES_FOUND_ERROR)
                    NetworkResult.Success(homes)
                }

                is NetworkResult.Error -> {
                    Timber.i(result.message, Constants.RETRIEVING_HOMES_BY_USER_ERROR)
                    if (result.message?.contains(Constants.STATUS_CODE_FORBIDDEN) == true) {
                        NetworkResult.Error(Constants.PERMISSION_DENIED_ERROR)
                    } else {
                        NetworkResult.Error(result.message ?: Constants.UNKNOWN_ERROR)
                    }
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_HOMES_BY_USER_ERROR)
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun saveHome(home: Home): NetworkResult<Home> {
        return try {
            val result = safeApiCall { homeService.saveHome(home.toHomeResponse()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { homeResponse ->
                        NetworkResult.Success(homeResponse.toHome())
                    } ?: NetworkResult.Error(Constants.HOME_NOT_CREATED_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(result.message, Constants.SAVING_HOME_ERROR)
                    NetworkResult.Error(Constants.HOME_NOT_CREATED_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.SAVING_HOME_ERROR)
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteHome(id: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { homeService.deleteHome(id) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }
                is NetworkResult.Error -> {
                    Timber.i(result.message, Constants.DELETING_HOME_ERROR)
                    NetworkResult.Error(Constants.HOME_NOT_DELETED_ERROR)
                }
                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.DELETING_HOME_ERROR)
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }
}