package com.example.homeinventoryapp.domain.usecases.item

import com.example.homeinventoryapp.data.repositories.ItemRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(itemId: Int) = itemRepository.getItemById(itemId)
}