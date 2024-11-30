package com.example.homeinventoryapp.domain.usecases.item

import com.example.homeinventoryapp.data.repositories.ItemRepository
import javax.inject.Inject

class GetItemsByCompartmentUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(compartmentId: Int) = itemRepository.getItemsByCompartment(compartmentId)
}