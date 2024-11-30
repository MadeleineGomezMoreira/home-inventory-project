package com.example.homeinventoryapp.domain.usecases.compartment

import com.example.homeinventoryapp.data.repositories.CompartmentRepository
import javax.inject.Inject

class DeleteCompartmentUseCase @Inject constructor(
    private val compartmentRepository: CompartmentRepository
) {
    operator fun invoke(id: Int) = compartmentRepository.deleteCompartment(id)
}