package com.example.homeinventoryapp.data.model.home

import com.google.gson.annotations.SerializedName

data class HomeRequestCreate(
    @SerializedName("homeName")
    val name: String,
    @SerializedName("ownedBy")
    val owner: Int,
)
