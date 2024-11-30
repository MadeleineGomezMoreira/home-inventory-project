package com.example.homeinventoryapp.domain.usecases.compartment

import com.example.homeinventoryapp.data.repositories.CompartmentRepository
import com.example.homeinventoryapp.domain.model.Compartment
import javax.inject.Inject

class SaveCompartmentUseCase @Inject constructor(
    private val compartmentRepository: CompartmentRepository
) {

    operator fun invoke(compartmentName: String, furnitureId: Int) =
        compartmentRepository.saveCompartment(
            Compartment(
                id = 0,
                name = compartmentName,
                furnId = furnitureId
            )
        )

}