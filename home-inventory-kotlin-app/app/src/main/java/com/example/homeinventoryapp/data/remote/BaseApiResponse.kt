package com.example.homeinventoryapp.data.remote

import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            if (response.code() == 404) {
                return NetworkResult.Error(Constants.DATA_NOT_FOUND_ERROR)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    suspend fun <T> safeApiCallNoBody(apiCall: suspend () -> Response<T>): NetworkResult<Boolean> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                return NetworkResult.Success(true)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }


    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error("ERROR: $errorMessage")
}

