package com.example.homeinventoryapp.data.model.room

import com.google.gson.annotations.SerializedName

data class RoomResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("roomName")
    val name: String,
    @SerializedName("homeId")
    val homeId: Int

)
