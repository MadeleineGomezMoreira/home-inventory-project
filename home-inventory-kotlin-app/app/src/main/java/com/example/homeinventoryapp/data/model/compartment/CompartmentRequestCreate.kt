package com.example.homeinventoryapp.data.model.compartment

import com.google.gson.annotations.SerializedName

data class CompartmentRequestCreate(
    @SerializedName("compName")
    val name: String,
    @SerializedName("furnId")
    val furnId: Int
)