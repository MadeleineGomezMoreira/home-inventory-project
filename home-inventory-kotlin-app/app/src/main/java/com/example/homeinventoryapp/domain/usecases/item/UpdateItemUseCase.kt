package com.example.homeinventoryapp.domain.usecases.item

import com.example.homeinventoryapp.data.repositories.ItemRepository
import com.example.homeinventoryapp.domain.model.ItemDetail
import javax.inject.Inject

class UpdateItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(item: ItemDetail) = itemRepository.updateItem(item)
}