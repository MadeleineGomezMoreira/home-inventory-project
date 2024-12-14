package com.example.homeinventoryapp.domain.usecases.item

import com.example.homeinventoryapp.data.model.item.ItemMoveRequest
import com.example.homeinventoryapp.data.repositories.ItemRepository
import javax.inject.Inject

class MoveItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(itemId: Int, compartmentId: Int) = itemRepository.moveItem(
        ItemMoveRequest(
            itemId = itemId,
            compId = compartmentId
        )
    )
}