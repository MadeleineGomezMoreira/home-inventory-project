package com.example.homeinventoryapp.data.model.item

import com.google.gson.annotations.SerializedName

data class ItemResponse (
    @SerializedName("id")
    val id: Int,
    @SerializedName("itemName")
    val name: String,
    @SerializedName("compId")
    val compId: Int,
)