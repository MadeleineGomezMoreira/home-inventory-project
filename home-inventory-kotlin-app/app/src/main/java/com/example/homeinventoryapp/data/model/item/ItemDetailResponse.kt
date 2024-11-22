package com.example.homeinventoryapp.data.model.item

import com.google.gson.annotations.SerializedName

data class ItemDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("itemName")
    val name: String,
    @SerializedName("compId")
    val compId: Int,
    @SerializedName("tags")
    val tags: List<TagResponse>
)
