package com.example.homeinventoryapp.data.repositories

import com.example.homeinventoryapp.data.remote.remoteDataSources.InvitationRemoteDataSource
import com.example.homeinventoryapp.domain.model.Invitation
import com.example.homeinventoryapp.domain.model.InvitationInfo
import com.example.homeinventoryapp.domain.model.InvitationToSend
import com.example.homeinventoryapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InvitationRepository @Inject constructor(
    private val invitationRemoteDataSource: InvitationRemoteDataSource
) {
    fun getInvitationInfo(id: Int): Flow<NetworkResult<InvitationInfo>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = invitationRemoteDataSource.getInvitationInfo(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getInvitationsByUser(id: Int): Flow<NetworkResult<List<Invitation>>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = invitationRemoteDataSource.getInvitationsByUser(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun sendInvitation(invitation: InvitationToSend): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = invitationRemoteDataSource.sendInvitation(invitation)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun acceptInvitation(id: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = invitationRemoteDataSource.acceptInvitation(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun declineInvitation(id: Int): Flow<NetworkResult<Unit>> {
        return flow {
            emit(NetworkResult.Loading())
            val result = invitationRemoteDataSource.declineInvitation(id)
            emit(result)
        }
    }

}