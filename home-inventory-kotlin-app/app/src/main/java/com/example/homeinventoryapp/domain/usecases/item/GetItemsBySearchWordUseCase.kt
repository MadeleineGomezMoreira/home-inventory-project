package com.example.homeinventoryapp.domain.usecases.item

import com.example.homeinventoryapp.data.repositories.ItemRepository
import javax.inject.Inject

class GetItemsBySearchWordUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(homeId: Int, searchWord: String) =
        itemRepository.getItemsBySearchWord(homeId, searchWord)
}