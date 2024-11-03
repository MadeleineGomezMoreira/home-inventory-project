package com.example.homeinventoryapp.data.model

import com.google.gson.annotations.SerializedName

data class MyHomesResponse(
    @SerializedName("OWNER")
    var ownedHomes: List<HomeResponse>,
    @SerializedName("MEMBER")
    var memberHomes: List<HomeResponse>
)