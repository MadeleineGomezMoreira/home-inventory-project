package com.example.homeinventoryapp.domain.model

data class InvitationToSend(
    val inviterId: Int,
    val inviteeUsername: String,
    val homeId: Int
)
