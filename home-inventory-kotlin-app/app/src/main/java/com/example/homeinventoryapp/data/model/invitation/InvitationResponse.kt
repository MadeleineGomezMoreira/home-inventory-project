package com.example.homeinventoryapp.data.model.invitation

import com.google.gson.annotations.SerializedName

data class InvitationResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("inviterId")
    var inviterId: Int,
    @SerializedName("inviteeId")
    var inviteeId: Int,
    @SerializedName("homeId")
    var homeId: Int
)