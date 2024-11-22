package com.example.homeinventoryapp.data.model.furniture

import com.google.gson.annotations.SerializedName

data class FurnitureRequestUpdate(
    @SerializedName("id")
    val id: Int,
    @SerializedName("furnName")
    val name: String
)
