package com.example.homeinventoryapp.data.model.invitation

data class InvitationRequestCreate(
    var inviterId: Int,
    var inviteeUsername: String,
    var homeId: Int,
)

