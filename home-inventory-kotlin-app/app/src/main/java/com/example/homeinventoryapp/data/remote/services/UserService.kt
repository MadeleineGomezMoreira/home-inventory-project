package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.HomeResponse
import com.example.homeinventoryapp.data.model.UserResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET(Constants.GET_USER_BY_USERNAME_PATH)
    suspend fun getUserByUsername(@Path(Constants.USERNAME_PARAM) username: String): Response<UserResponse>

    @POST(Constants.GET_USER_BY_ID_PATH)
    suspend fun getUserById(@Path(Constants.ID_PARAM) id: Int): Response<UserResponse>

}