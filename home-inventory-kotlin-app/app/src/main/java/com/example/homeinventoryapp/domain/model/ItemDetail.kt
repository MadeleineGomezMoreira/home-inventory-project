package com.example.homeinventoryapp.domain.model

data class ItemDetail(
    val id: Int,
    val name: String,
    val compId: Int,
    val tags: List<ItemTag>,
)
