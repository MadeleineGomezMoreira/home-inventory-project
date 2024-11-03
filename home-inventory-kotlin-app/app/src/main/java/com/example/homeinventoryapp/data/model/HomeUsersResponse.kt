package com.example.homeinventoryapp.data.model

import com.google.gson.annotations.SerializedName

data class HomeUsersResponse (
    @SerializedName("OWNER")
    var owner: List<UserResponse>,
    @SerializedName("MEMBERS")
    var members : List<UserResponse>
)