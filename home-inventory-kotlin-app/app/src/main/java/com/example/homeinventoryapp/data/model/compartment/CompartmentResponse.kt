package com.example.homeinventoryapp.data.model.compartment

import com.google.gson.annotations.SerializedName

data class CompartmentResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("compName")
    val name: String,
    @SerializedName("furnId")
    val furnId: Int
)
