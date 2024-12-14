package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.item.ItemDetailResponse
import com.example.homeinventoryapp.data.model.item.ItemMoveRequest
import com.example.homeinventoryapp.data.model.item.ItemRequestCreate
import com.example.homeinventoryapp.data.model.item.ItemRequestUpdate
import com.example.homeinventoryapp.data.model.item.ItemResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemService {

    @GET(Constants.GET_ITEMS_BY_COMPARTMENT_PATH)
    suspend fun getItemsByCompartment(@Path(Constants.ID_PARAM) id: Int): Response<List<ItemResponse>>

    @GET(Constants.GET_ITEMS_BY_STRING_PATH)
    suspend fun getItemsByString(
        //homeId
        @Path(Constants.ID_PARAM) id: Int,
        @Query("search_word") searchWord: String
    ): Response<List<ItemResponse>>

    @GET(Constants.GET_ITEM_BY_ID_PATH)
    suspend fun getItemById(@Path(Constants.ID_PARAM) id: Int): Response<ItemDetailResponse>

    @POST(Constants.REGISTER_ITEM_PATH)
    suspend fun saveItem(@Body item: ItemRequestCreate): Response<ItemDetailResponse>

    @PUT(Constants.UPDATE_ITEM_PATH)
    suspend fun updateItem(@Body item: ItemRequestUpdate): Response<ItemDetailResponse>

    @DELETE(Constants.DELETE_ITEM_PATH)
    suspend fun deleteItem(@Path(Constants.ID_PARAM) id: Int): Response<Unit>

    @GET(Constants.GET_ITEM_ROUTE_PATH)
    suspend fun getItemRoute(@Path(Constants.ID_PARAM) id: Int): Response<String>

    @PUT(Constants.MOVE_ITEM_ROUTE_PATH)
    suspend fun moveItem(@Body item: ItemMoveRequest): Response<Unit>

}