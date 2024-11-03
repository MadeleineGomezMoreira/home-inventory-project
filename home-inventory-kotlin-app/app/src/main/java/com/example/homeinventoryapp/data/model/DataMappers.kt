package com.example.homeinventoryapp.data.model

import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.HomeUsers
import com.example.homeinventoryapp.domain.model.MyHomes
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

fun Home.toHomeRequest(): HomeRequest {
    return HomeRequest(
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

fun MyHomesResponse.toMyHomes(): MyHomes {
    return MyHomes(
        ownedHomes = ownedHomes.map { it.toHome() },
        memberHomes = memberHomes.map { it.toHome() }
    )
}

fun HomeUsersResponse.toHomeUsers(): HomeUsers {
    return HomeUsers(
        owner = owner.toUser(),
        members = members.map { it.toUser() }
    )

}