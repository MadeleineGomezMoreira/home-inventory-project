package com.example.homeinventoryapp.data.remote.services

import com.example.homeinventoryapp.data.model.invitation.InvitationRequestCreate
import com.example.homeinventoryapp.data.model.invitation.InvitationResponse
import com.example.homeinventoryapp.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InvitationService {

    @GET(Constants.GET_INVITATIONS_BY_USER)
    suspend fun getInvitationsByUser(@Path(Constants.ID_PARAM) id: Int) : Response<List<InvitationResponse>>

    @POST(Constants.SEND_INVITATION_PATH)
    suspend fun sendInvitation(@Body invitation: InvitationRequestCreate) : Response<Unit>

    @POST(Constants.ACCEPT_INVITATION_PATH)
    suspend fun acceptInvitation(@Path(Constants.ID_PARAM) id: Int) : Response<Unit>

    @DELETE(Constants.DECLINE_INVITATION_PATH)
    suspend fun declineInvitation(@Path(Constants.ID_PARAM) id: Int) : Response<Unit>
}
