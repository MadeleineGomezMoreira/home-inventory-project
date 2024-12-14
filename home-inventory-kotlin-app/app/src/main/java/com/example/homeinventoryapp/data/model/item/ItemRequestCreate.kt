package com.example.homeinventoryapp.data.model.item

import com.google.gson.annotations.SerializedName

data class ItemRequestCreate(
    @SerializedName("itemName")
    val name: String,
    @SerializedName("compId")
    val compartmentId: Int,
    @SerializedName("tags")
    val tags: List<String>
)