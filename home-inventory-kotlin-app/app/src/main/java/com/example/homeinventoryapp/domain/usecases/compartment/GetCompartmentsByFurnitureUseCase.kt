package com.example.homeinventoryapp.domain.usecases.compartment

import com.example.homeinventoryapp.data.repositories.CompartmentRepository
import javax.inject.Inject

class GetCompartmentsByFurnitureUseCase @Inject constructor(
    private val compartmentRepository: CompartmentRepository
) {

    operator fun invoke(furnitureId: Int) =
        compartmentRepository.getCompartmentsByFurniture(furnitureId)

}