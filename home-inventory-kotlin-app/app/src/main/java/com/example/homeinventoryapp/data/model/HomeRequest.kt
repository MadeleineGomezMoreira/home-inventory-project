package com.example.homeinventoryapp.data.model

import com.google.gson.annotations.SerializedName

data class HomeRequest(
    @SerializedName("homeName")
    val name: String,
    @SerializedName("ownedBy")
    val owner: Int,
)
