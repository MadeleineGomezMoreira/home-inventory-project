package com.example.homeinventoryapp.domain.model

data class Invitation(
    val id: Int,
    val inviterId: Int,
    val inviteeId: Int,
    val homeId: Int,
)