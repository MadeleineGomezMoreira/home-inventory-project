package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.homeusers.HomeUsersResponse
import com.example.homeinventoryapp.data.model.user.UserResponse
import com.example.homeinventoryapp.domain.model.LoginDTO
import com.example.homeinventoryapp.domain.model.RegisterDTO
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET(Constants.GET_USER_BY_USERNAME_PATH)
    suspend fun getUserByUsername(@Path(Constants.USERNAME_PARAM) username: String): Response<UserResponse>

    @GET(Constants.GET_USER_BY_ID_PATH)
    suspend fun getUserById(@Path(Constants.ID_PARAM) id: Int): Response<UserResponse>

    @GET(Constants.GET_USERS_BY_HOME_ID_PATH)
    suspend fun getUsersByHomeId(@Path(Constants.ID_PARAM) id: Int): Response<HomeUsersResponse>

    @POST(Constants.USERS_REGISTER_PATH)
    suspend fun registerUser(@Body user: RegisterDTO): Response<Unit>

    @POST(Constants.USERS_LOGIN_PATH)
    suspend fun loginUser(@Body user: LoginDTO): Response<Int>

    @DELETE(Constants.DELETE_USER_PATH)
    suspend fun deleteUser(@Path(Constants.ID_PARAM) id: Int): Response<Unit>


}