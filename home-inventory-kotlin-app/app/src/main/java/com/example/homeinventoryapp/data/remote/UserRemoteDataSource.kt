package com.example.homeinventoryapp.data.remote

import com.example.homeinventoryapp.data.model.toHomeUsers
import com.example.homeinventoryapp.data.model.toUser
import com.example.homeinventoryapp.data.remote.services.UserService
import com.example.homeinventoryapp.domain.model.HomeUsers
import com.example.homeinventoryapp.domain.model.User
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService,
) : BaseApiResponse() {

    suspend fun getUserByUsername(username: String): NetworkResult<User> {
        return try {
            val result = safeApiCall { userService.getUserByUsername(username) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { userResponse ->
                        NetworkResult.Success(userResponse.toUser())
                    } ?: NetworkResult.Error(Constants.NO_USER_FOUND_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(result.message, Constants.RETRIEVING_USER_BY_USERNAME_ERROR)
                    NetworkResult.Error(
                        result.message ?: Constants.RETRIEVING_USER_BY_USERNAME_ERROR
                    )
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_USER_BY_USERNAME_ERROR)
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getUsersByHomeId(id: Int): NetworkResult<HomeUsers> {
        return try {
            val result = safeApiCall { userService.getUsersByHomeId(id) }
            when (result) {
                is NetworkResult.Success ->
                    result.data?.let { homeUsersResponse ->
                        NetworkResult.Success(homeUsersResponse.toHomeUsers())
                    } ?: NetworkResult.Error(Constants.NO_MEMBERS_FOUND_ERROR)

                is NetworkResult.Error -> {
                    Timber.i(result.message, Constants.RETRIEVING_USERS_BY_HOME_ID_ERROR)
                    NetworkResult.Error(
                        result.message ?: Constants.NO_MEMBERS_FOUND_ERROR
                    )
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_USERS_BY_HOME_ID_ERROR)
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getUserById(id: Int): NetworkResult<User> {
        return try {
            val result = safeApiCall { userService.getUserById(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { userResponse ->
                        NetworkResult.Success(userResponse.toUser())
                    } ?: NetworkResult.Error(Constants.NO_USER_FOUND_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(result.message, Constants.RETRIEVING_USER_BY_ID_ERROR)
                    NetworkResult.Error(
                        result.message ?: Constants.RETRIEVING_USER_BY_ID_ERROR
                    )
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_USER_BY_ID_ERROR)
            return NetworkResult.Error(e.message ?: e.toString())
        }
    }
}