package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.home.HomeRequestCreate
import com.example.homeinventoryapp.data.model.home.HomeRequestUpdate
import com.example.homeinventoryapp.data.model.home.HomeResponse
import com.example.homeinventoryapp.data.model.home.MyHomesResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HomeService {

    @GET(Constants.GET_HOMES_BY_USER_PATH)
    suspend fun getHomesByUser(@Path(Constants.ID_PARAM) id: Int): Response<MyHomesResponse>

    @GET(Constants.GET_HOME_PATH)
    suspend fun getHome(@Path(Constants.ID_PARAM) id: Int): Response<HomeResponse>

    @POST(Constants.REGISTER_HOME_PATH)
    suspend fun saveHome(@Body home: HomeRequestCreate): Response<HomeResponse>

    @PUT(Constants.UPDATE_HOME_PATH)
    suspend fun updateHome(@Body home: HomeRequestUpdate): Response<HomeResponse>

    @DELETE(Constants.DELETE_HOME_PATH)
    suspend fun deleteHome(@Path(Constants.ID_PARAM) id: Int): Response<Unit>


}