package com.example.homeinventoryapp.domain.model

data class MyHomes(
    var ownedHomes: List<Home>,
    var memberHomes: List<Home>
)