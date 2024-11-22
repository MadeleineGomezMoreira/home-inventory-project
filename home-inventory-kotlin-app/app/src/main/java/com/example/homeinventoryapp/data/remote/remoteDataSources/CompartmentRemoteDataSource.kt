package com.example.homeinventoryapp.data.remote.remoteDataSources

import com.example.homeinventoryapp.data.remote.BaseApiResponse
import com.example.homeinventoryapp.data.remote.services.CompartmentService
import javax.inject.Inject

class CompartmentRemoteDataSource @Inject constructor(
    private val compartmentService: CompartmentService,
): BaseApiResponse() {

    //TODO: implement methods
}