package com.example.homeinventoryapp.data.model.home

import com.google.gson.annotations.SerializedName

data class HomeRequestUpdate(
    @SerializedName("id")
    val id: Int,
    @SerializedName("homeName")
    val name: String,
)