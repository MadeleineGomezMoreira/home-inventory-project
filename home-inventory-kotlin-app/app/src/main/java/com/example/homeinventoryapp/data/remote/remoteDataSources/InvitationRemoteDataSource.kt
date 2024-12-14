package com.example.homeinventoryapp.data.remote.remoteDataSources

import com.example.homeinventoryapp.data.model.toInvitation
import com.example.homeinventoryapp.data.model.toInvitationInfo
import com.example.homeinventoryapp.data.model.toInvitationRequestCreate
import com.example.homeinventoryapp.data.remote.BaseApiResponse
import com.example.homeinventoryapp.data.remote.services.InvitationService
import com.example.homeinventoryapp.domain.model.Invitation
import com.example.homeinventoryapp.domain.model.InvitationInfo
import com.example.homeinventoryapp.domain.model.InvitationToSend
import com.example.homeinventoryapp.utils.Constants
import com.example.homeinventoryapp.utils.NetworkResult
import timber.log.Timber
import javax.inject.Inject

class InvitationRemoteDataSource @Inject constructor(
    private val invitationService: InvitationService
) : BaseApiResponse() {

    suspend fun getInvitationInfo(id: Int): NetworkResult<InvitationInfo> {
        return try {
            val result = safeApiCall { invitationService.getInvitationInfo(id) }
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.toInvitationInfo()?.let { NetworkResult.Success(it) }
                        ?: NetworkResult.Error(Constants.RETRIEVING_INVITATION_INFO_ERROR)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_INVITATION_INFO_ERROR
                    )
                    NetworkResult.Error(
                        result.message ?: Constants.RETRIEVING_INVITATION_INFO_ERROR
                    )
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_INVITATION_INFO_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun getInvitationsByUser(id: Int): NetworkResult<List<Invitation>> {
        return try {
            val result = safeApiCall { invitationService.getInvitationsByUser(id) }
            when (result) {
                is NetworkResult.Success -> {
                    val mappedInvitations = result.data?.map { invitationResponse ->
                        invitationResponse.toInvitation()
                    } ?: emptyList()
                    NetworkResult.Success(mappedInvitations)
                }


                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.RETRIEVING_INVITATIONS_BY_USER_ERROR
                    )
                    NetworkResult.Error(
                        result.message ?: Constants.RETRIEVING_INVITATIONS_BY_USER_ERROR
                    )
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.RETRIEVING_INVITATIONS_BY_USER_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun sendInvitation(invitation: InvitationToSend): NetworkResult<Unit> {
        return try {
            val result =
                safeApiCall { invitationService.sendInvitation(invitation.toInvitationRequestCreate()) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.SENDING_INVITATION_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.SENDING_INVITATION_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.SENDING_INVITATION_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun acceptInvitation(id: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { invitationService.acceptInvitation(id) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.ACCEPTING_INVITATION_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.ACCEPTING_INVITATION_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.ACCEPTING_INVITATION_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }

    suspend fun declineInvitation(id: Int): NetworkResult<Unit> {
        return try {
            val result = safeApiCall { invitationService.declineInvitation(id) }
            when (result) {
                is NetworkResult.Success -> {
                    NetworkResult.Success(Unit)
                }

                is NetworkResult.Error -> {
                    Timber.i(
                        result.message ?: Constants.EMPTY_MESSAGE,
                        Constants.DECLINING_INVITATION_ERROR
                    )
                    NetworkResult.Error(result.message ?: Constants.DECLINING_INVITATION_ERROR)
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, Constants.DECLINING_INVITATION_ERROR)
            NetworkResult.Error(e.message ?: e.toString())
        }
    }


}