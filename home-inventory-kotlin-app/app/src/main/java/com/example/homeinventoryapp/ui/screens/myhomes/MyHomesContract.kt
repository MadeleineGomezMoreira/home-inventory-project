package com.example.homeinventoryapp.ui.screens.myhomes

import com.example.homeinventoryapp.domain.model.Home

class MyHomesContract {

    data class MyHomesState(
        val ownerHomes: List<Home> = emptyList(),
        val memberHomes: List<Home> = emptyList(),
        val homeId: Int? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val showCreateDialogue: Boolean = false
    )

    sealed class MyHomesEvent {
        data class CreateHome(val homeName: String, val userId: Int?) : MyHomesEvent()
        data class GetHomes(val userId: Int) : MyHomesEvent()
        data object ErrorDisplayed : MyHomesEvent()
        data class HomeClicked(val home: Home) : MyHomesEvent()
        data object ClearHome : MyHomesEvent()
        data object ClearDialogue : MyHomesEvent()
        data object ShowDialogue : MyHomesEvent()
    }


}