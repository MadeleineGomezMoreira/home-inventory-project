package com.example.homeinventoryapp.domain.usecases.compartment

import com.example.homeinventoryapp.data.repositories.CompartmentRepository
import com.example.homeinventoryapp.domain.model.Compartment
import javax.inject.Inject

class UpdateCompartmentUseCase @Inject constructor(
    private val compartmentRepository: CompartmentRepository
) {

    operator fun invoke(compartmentId: Int, compartmentName: String) =
        compartmentRepository.updateCompartment(
            Compartment(
                id = compartmentId,
                name = compartmentName,
                furnId = 0
            )
        )

}