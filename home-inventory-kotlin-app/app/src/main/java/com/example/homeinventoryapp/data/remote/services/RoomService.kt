package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.room.RoomRequestCreate
import com.example.homeinventoryapp.data.model.room.RoomRequestUpdate
import com.example.homeinventoryapp.data.model.room.RoomResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoomService {

    @POST(Constants.REGISTER_ROOM_PATH)
    suspend fun saveRoom(@Body room: RoomRequestCreate): Response<RoomResponse>

    @GET(Constants.GET_ROOM_BY_ID_PATH)
    suspend fun getRoom(@Path(Constants.ID_PARAM) id: Int): Response<RoomResponse>

    @GET(Constants.GET_ROOMS_BY_HOME_PATH)
    suspend fun getRoomsByHome(@Path(Constants.ID_PARAM) id: Int): Response<List<RoomResponse>>

    @PUT(Constants.UPDATE_ROOM_PATH)
    suspend fun updateRoom(@Body room: RoomRequestUpdate): Response<RoomResponse>

    @DELETE(Constants.DELETE_ROOM_PATH)
    suspend fun deleteRoom(@Path(Constants.ID_PARAM) id: Int): Response<Unit>
}