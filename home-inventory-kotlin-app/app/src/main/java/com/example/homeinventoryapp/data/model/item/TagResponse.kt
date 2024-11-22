package com.example.homeinventoryapp.data.model.item

import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("tagName")
    val name: String,
    @SerializedName("homeId")
    val homeId: Int
)
