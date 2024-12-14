package com.example.homeinventoryapp.data.model.invitation

import com.google.gson.annotations.SerializedName

data class InvitationInfoResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("inviterName")
    var inviterName: String,
    @SerializedName("homeName")
    var homeName: String,
)