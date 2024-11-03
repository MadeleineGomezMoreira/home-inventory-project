package com.example.homeinventoryapp.data.model

import com.google.gson.annotations.SerializedName

data class HomeUsersResponse(
    @SerializedName("OWNER")
    var owner: UserResponse,
    @SerializedName("MEMBER")
    var members: List<UserResponse>
)