package com.example.homeinventoryapp.data.model.furniture

import com.google.gson.annotations.SerializedName

data class FurnitureResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("furnName")
    val name: String,
    @SerializedName("roomId")
    val roomId: Int
)