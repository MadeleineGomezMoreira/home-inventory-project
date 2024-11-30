package com.example.homeinventoryapp.data.remote.remoteDataSources

import com.example.homeinventoryapp.data.model.toCompartment
import com.example.homeinventoryapp.data.model.toCompartmentRequestCreate
import com.example.homeinventoryapp.data.model.toCompartmentRequestUpdate
import com.example.homeinventoryapp.data.remote.BaseApiResponse
import com.example.homeinventoryapp.data.remote.services.CompartmentService
import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class CompartmentRemoteDataSource @Inject constructor(
    private val compartmentService: CompartmentService,
) : BaseApiResponse() {

    suspend fun getCompartmentsByFurniture(id: Int): NetworkResult<List<Compartment>> {
        return try {
            val result = safeApiCall { compartmentService.getCompartmentsByFurniture(id) }
            when (result) {
                is NetworkResult.Success -> {
                    val mappedCompartments = result.data?.map { it.toCompartment() } ?: emptyList()
                    NetworkResult.Success(mappedCompartments)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_COMPARTMENTS_BY_FURNITURE_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_COMPARTMENTS_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_COMPARTMENTS_BY_FURNITURE_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getCompartmentById(id: Int): NetworkResult<Compartment> {
        return try {
            val result = safeApiCall { compartmentService.getCompartment(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toCompartment()?.let {
                        NetworkResult.Success(it)

                    } ?: NetworkResult.Error(Constants.RETRIEVING_COMPARTMENT_BY_ID_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_COMPARTMENT_BY_ID_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.NO_COMPARTMENTS_FOUND_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_COMPARTMENT_BY_ID_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun saveCompartment(compartment: Compartment): NetworkResult<Compartment> {
        return try {
            val result =
                safeApiCall { compartmentService.saveCompartment(compartment.toCompartmentRequestCreate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toCompartment()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error(Constants.SAVING_COMPARTMENT_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.SAVING_COMPARTMENT_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.SAVING_COMPARTMENT_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.SAVING_COMPARTMENT_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun updateCompartment(compartment: Compartment): NetworkResult<Compartment> {
        return try {
            val result =
                safeApiCall { compartmentService.updateCompartment(compartment.toCompartmentRequestUpdate()) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toCompartment()?.let {
                        NetworkResult.Success(it)
                    } ?: NetworkResult.Error(Constants.UPDATING_COMPARTMENT_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.UPDATING_COMPARTMENT_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.UPDATING_COMPARTMENT_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.UPDATING_COMPARTMENT_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun deleteCompartment(compartmentId: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { compartmentService.deleteCompartment(compartmentId) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.DELETING_COMPARTMENT_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.DELETING_COMPARTMENT_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.DELETING_COMPARTMENT_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }
}
