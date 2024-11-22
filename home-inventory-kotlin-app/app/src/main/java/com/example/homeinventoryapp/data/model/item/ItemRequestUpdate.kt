package com.example.homeinventoryapp.data.model.item

import com.google.gson.annotations.SerializedName

data class ItemRequestUpdate (
    @SerializedName("id")
    val id: Int,
    @SerializedName("itemName")
    val name: String,
    @SerializedName("compId")
    val compartmentId: Int,
    @SerializedName("tags")
    val tags: List<TagRequest>
)