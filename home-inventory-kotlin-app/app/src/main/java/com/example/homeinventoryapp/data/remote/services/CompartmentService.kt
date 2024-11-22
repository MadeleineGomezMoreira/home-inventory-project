package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.compartment.CompartmentRequestCreate
import com.example.homeinventoryapp.data.model.compartment.CompartmentRequestUpdate
import com.example.homeinventoryapp.data.model.compartment.CompartmentResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompartmentService {

    @POST(Constants.REGISTER_COMPARTMENT_PATH)
    suspend fun saveCompartment(@Body compartment: CompartmentRequestCreate): Response<CompartmentResponse>

    @GET(Constants.GET_COMPARTMENTS_BY_FURNITURE_PATH)
    suspend fun getCompartmentsByFurniture(@Path(Constants.ID_PARAM) id: Int): Response<List<CompartmentResponse>>

    @PUT(Constants.UPDATE_COMPARTMENT_PATH)
    suspend fun updateCompartment(@Body compartment: CompartmentRequestUpdate): Response<CompartmentResponse>

    @DELETE(Constants.DELETE_COMPARTMENT_PATH)
    suspend fun deleteCompartment(@Path(Constants.ID_PARAM) id: Int): Response<Unit>



}