package com.example.homeinventoryapp.data.model.homeusers

import com.example.homeinventoryapp.data.model.user.UserResponse
import com.google.gson.annotations.SerializedName

data class HomeUsersResponse(
    @SerializedName("OWNER")
    var owner: UserResponse,
    @SerializedName("MEMBER")
    var members: List<UserResponse>
)