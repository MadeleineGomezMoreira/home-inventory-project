package com.example.homeinventoryapp.data.model.invitation

data class InvitationRequestCreate(
    var inviterId: Int,
    var inviteeId: Int,
    var homeId: Int,
)

