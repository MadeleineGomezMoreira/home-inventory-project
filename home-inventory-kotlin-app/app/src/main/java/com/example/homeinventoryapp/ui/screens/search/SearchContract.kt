package com.example.homeinventoryapp.ui.screens.search

import com.example.homeinventoryapp.domain.model.Item

class SearchContract {

    data class SearchState(
        val items: List<Item> = emptyList(),
        val searchWord: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val itemId: Int? = null,
    )

    sealed class SearchEvent {
        data class GetSearch(val homeId: Int, val searchWord: String) : SearchEvent()
        data class UpdateSearchWord(val searchWord: String) : SearchEvent()
        data class ItemClicked(val item: Item) : SearchEvent()
        data object ErrorDisplayed : SearchEvent()
        data object ClearItem : SearchEvent()
    }


}