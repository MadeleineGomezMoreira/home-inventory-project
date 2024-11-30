package com.example.homeinventoryapp.domain.model

data class HomeUsers(
    val owner: User,
    val members: List<User>
)