package com.example.homeinventoryapp.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertInvitation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homeinventoryapp.R
import com.example.homeinventoryapp.domain.model.Invitation
import com.example.homeinventoryapp.domain.model.User
import com.example.homeinventoryapp.ui.common.ClickableSmallCard
import com.example.homeinventoryapp.ui.common.CustomText
import com.example.homeinventoryapp.ui.common.CustomTextBold
import com.example.homeinventoryapp.ui.common.DefaultIcon
import com.example.homeinventoryapp.ui.common.DefaultImage
import com.example.homeinventoryapp.ui.common.InvitationDialog
import com.example.homeinventoryapp.ui.common.ListSmallCard
import com.example.homeinventoryapp.ui.common.LoadingProgressComponent
import com.example.homeinventoryapp.ui.common.ShowSnackbarMessage
import com.example.homeinventoryapp.ui.common.di.UserSession
import com.example.homeinventoryapp.utils.Constants

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    onAccountDeleted: () -> Unit,
) {

    val uiState by viewModel.state.collectAsState()
    val userId = UserSession.userId ?: Constants.ZERO_CODE

    LaunchedEffect(userId) {
        viewModel.handleEvent(AccountContract.AccountEvent.GetUser(userId))
        viewModel.handleEvent(AccountContract.AccountEvent.GetInvitations(userId))
    }

    if (uiState.userWasDeleted) {
        viewModel.handleEvent(AccountContract.AccountEvent.ClearUserWasDeleted)
        onAccountDeleted()
    }

    if (uiState.invitationStatusChanged) {
        viewModel.handleEvent(AccountContract.AccountEvent.ClearInvitationStatus)
        viewModel.handleEvent(AccountContract.AccountEvent.GetInvitations(userId))
    }

    if (uiState.showInvitationDialogue) {
        InvitationDialog(
            onDismiss = {
                viewModel.handleEvent(AccountContract.AccountEvent.ClearInvitationDialogue)
            },
            invitationInfo = uiState.invitationInfo,
            onAcceptInvitation = { invitationId ->
                viewModel.handleEvent(AccountContract.AccountEvent.AcceptInvitation(invitationId))
            },
            onDeclineInvitation = { invitationId ->
                viewModel.handleEvent(AccountContract.AccountEvent.RejectInvitation(invitationId))
            }
        )
    }

    AccountContent(
        user = uiState.user,
        invitations = uiState.invitations,
        onAccountDeleted = onAccountDeleted,
        error = uiState.error,
        isLoading = uiState.isLoading,
        bottomNavigationBar = bottomNavigationBar,
        topBar = topBar,
        onInvitationClicked = { invitationId ->
            viewModel.handleEvent(AccountContract.AccountEvent.GetInvitationInfo(invitationId))
            viewModel.handleEvent(AccountContract.AccountEvent.ShowInvitationDialogue)
        }
    )

}

@Composable
fun AccountContent(
    user: User?,
    invitations: List<Invitation>?,
    onAccountDeleted: () -> Unit,
    error: String?,
    isLoading: Boolean = false,
    errorShown: () -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    onInvitationClicked: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = topBar,
        bottomBar = bottomNavigationBar
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultImage(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.alpha = 0.7f
                    }
                    .blur(10.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.6f), Color.Transparent)
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextBold(text = stringResource(R.string.username_upper), modifier = Modifier.align(Alignment.Start))
            CustomText(text = user?.username ?: Constants.EMPTY_STRING, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextBold(text = stringResource(R.string.email_upper), modifier = Modifier.align(Alignment.Start))
            CustomText(text = user?.email ?: Constants.EMPTY_STRING, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))
            if (invitations?.isNotEmpty() == true) {
                Text(text = stringResource(R.string.invitations), style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                InvitationsListSmallCard(
                    invitations = invitations,
                    onInvitationClicked = { invitation ->
                        onInvitationClicked(invitation.id)
                    }
                )
            }
            if (invitations.isNullOrEmpty()) {
                CustomTextBold(
                    text = stringResource(R.string.no_invitations_found),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Button(
                onClick = { onAccountDeleted() },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(ButtonDefaults.buttonColors().disabledContainerColor),
                content = {
                    Text(
                        text = stringResource(R.string.delete_account_upper),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp),
                        fontSize = 20.sp
                    )
                }
            )
        }
        ShowSnackbarMessage(message = error, snackbarHostState = snackbarHostState) {
            errorShown()
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingProgressComponent(showComponent = true)
            }
        }
    }
}

@Composable
fun InvitationsListSmallCard(
    invitations: List<Invitation>,
    onInvitationClicked: (Invitation) -> Unit
) {
    ListSmallCard(
        items = invitations,
        onItemClicked = onInvitationClicked
    ) { user ->
        ClickableSmallCard(
            item = user,
            onItemClicked = onInvitationClicked,
            textContent = { stringResource(R.string.pending_invitation) },
            iconContent = {
                DefaultIcon(
                    imageVector = Icons.Default.InsertInvitation,
                    contentDescription = Constants.INSERT_INVITATION_ICON_DESCRIPTION,
                    size = 60.dp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        )
    }
}