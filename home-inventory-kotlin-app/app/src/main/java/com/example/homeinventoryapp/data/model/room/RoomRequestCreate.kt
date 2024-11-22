package com.example.homeinventoryapp.data.model.room

import com.google.gson.annotations.SerializedName

data class RoomRequestCreate (
    @SerializedName("roomName")
    val name: String,
    @SerializedName("homeId")
    val homeId: Int

)