package com.example.homeinventoryapp.data.model

import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.User

fun HomeResponse.toHome(): Home {
    return Home(
        id = id,
        name = name,
        owner = owner
    )
}

fun Home.toHomeResponse(): HomeResponse {
    return HomeResponse(
        id = id,
        name = name,
        owner = owner
    )
}

fun UserResponse.toUser(): User {
    return User(
        id = id,
        username = username,
        email = email
    )
}