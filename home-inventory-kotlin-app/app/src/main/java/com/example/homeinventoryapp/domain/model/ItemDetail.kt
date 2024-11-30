package com.example.homeinventoryapp.domain.model

data class ItemDetail(
    val id: Int,
    var name: String,
    val compId: Int,
    var tags: List<ItemTag>,
)
