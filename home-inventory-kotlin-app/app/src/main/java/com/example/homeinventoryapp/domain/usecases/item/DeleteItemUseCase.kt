package com.example.homeinventoryapp.domain.usecases.item

import com.example.homeinventoryapp.data.repositories.ItemRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(id: Int) = itemRepository.deleteItem(id)
}