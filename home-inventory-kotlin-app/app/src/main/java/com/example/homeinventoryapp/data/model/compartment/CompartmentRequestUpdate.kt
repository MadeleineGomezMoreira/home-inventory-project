package com.example.homeinventoryapp.data.model.compartment

import com.google.gson.annotations.SerializedName

data class CompartmentRequestUpdate(
    @SerializedName("id")
    val id: Int,
    @SerializedName("compName")
    val name: String
)
