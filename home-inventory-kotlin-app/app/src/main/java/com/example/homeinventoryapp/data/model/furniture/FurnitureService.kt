package com.example.homeinventoryapp.data.model.furniture

import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FurnitureService {

    @POST(Constants.REGISTER_FURNITURE_PATH)
    suspend fun saveFurniture(@Body furniture: FurnitureRequestCreate): Response<FurnitureResponse>

    @GET(Constants.GET_FURNITURE_BY_ROOM_PATH)
    suspend fun getFurnitureByRoom(@Path(Constants.ID_PARAM) id: Int): Response<List<FurnitureResponse>>

    @GET(Constants.GET_FURNITURE_BY_ID_PATH)
    suspend fun getFurnitureById(@Path(Constants.ID_PARAM) id: Int): Response<FurnitureResponse>

    @PUT(Constants.UPDATE_FURNITURE_PATH)
    suspend fun updateFurniture(@Body furniture: FurnitureRequestUpdate): Response<FurnitureResponse>

    @DELETE(Constants.DELETE_FURNITURE_PATH)
    suspend fun deleteFurniture(@Path(Constants.ID_PARAM) id: Int): Response<Unit>


}