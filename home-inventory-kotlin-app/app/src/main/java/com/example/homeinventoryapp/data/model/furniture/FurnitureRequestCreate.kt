package com.example.homeinventoryapp.data.model.furniture

import com.google.gson.annotations.SerializedName

data class FurnitureRequestCreate(
    @SerializedName("furnName")
    val name: String,
    @SerializedName("roomId")
    val roomId: Int
)
