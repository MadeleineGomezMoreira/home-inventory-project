package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.HomeRequest
import com.example.homeinventoryapp.data.model.HomeResponse
import com.example.homeinventoryapp.data.model.MyHomesResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeService {

    @GET(Constants.GET_HOMES_BY_USER_PATH)
    suspend fun getHomesByUser(@Path(Constants.ID_PARAM) id: Int): Response<MyHomesResponse>

    @GET(Constants.GET_HOME_PATH)
    suspend fun getHome(@Path(Constants.ID_PARAM) id: Int): Response<HomeResponse>

    @POST(Constants.REGISTER_HOME_PATH)
    suspend fun saveHome(@Body home: HomeRequest): Response<HomeResponse>

    @DELETE(Constants.DELETE_HOME_PATH)
    suspend fun deleteHome(@Path(Constants.ID_PARAM) id: Int): Response<Unit>


}