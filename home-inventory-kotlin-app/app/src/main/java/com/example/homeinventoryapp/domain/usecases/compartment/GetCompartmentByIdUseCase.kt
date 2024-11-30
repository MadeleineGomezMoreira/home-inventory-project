package com.example.homeinventoryapp.domain.usecases.compartment

import com.example.homeinventoryapp.data.repositories.CompartmentRepository
import javax.inject.Inject

class GetCompartmentByIdUseCase @Inject constructor(
    private val compartmentRepository: CompartmentRepository
) {
    operator fun invoke(compartmentId: Int) = compartmentRepository.getCompartment(compartmentId)
}