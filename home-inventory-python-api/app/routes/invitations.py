from fastapi import APIRouter, Depends, status
from app.schemas.invitation import InvitationResponse, InvitationRequest, InvitationInfoResponse
from app.database.db_engine import get_db
from app.repositories.invitation_repository import (
    create_invitation,
    take_up_invitation,
    reject_invitation,
    get_received_invitations,
    get_invitation_info
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()

@router.get("/invitations/info/{invitation_id}", response_model= InvitationInfoResponse)
async def get_info(invitation_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve information about a specific invitation.

    This endpoint fetches detailed information about a specific invitation using its 
    unique ID. It returns the invitation details as an `InvitationInfoResponse`.

    Args:
        invitation_id (int): The ID of the invitation to retrieve information for.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        InvitationInfoResponse: A response containing the invitation information.
    """
    info = await get_invitation_info(db, invitation_id)
    return InvitationInfoResponse.model_validate(info)

@router.get("/invitations/{user_id}", response_model=list[InvitationResponse])
async def read_received_invitations(user_id: int, db: AsyncSession = Depends(get_db)):
    """Get all received invitations for a specific user.

    This endpoint retrieves a list of invitations received by the user with the 
    provided `user_id`. It returns a list of `InvitationResponse` models for each 
    invitation received by the user.

    Args:
        user_id (int): The ID of the user whose received invitations are being fetched.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[InvitationResponse]: A list of received invitations for the specified user.
    """
    invitations = await get_received_invitations(db, user_id)
    return [
        InvitationResponse.model_validate(invitation.to_dict())
        for invitation in invitations
    ]

@router.post("/invitations/send/", status_code=status.HTTP_201_CREATED)
async def send_invitation(
    invitation: InvitationRequest, db: AsyncSession = Depends(get_db)
):
    """Send an invitation to a user.

    This endpoint allows the creation and sending of an invitation to a user. The 
    invitation details are provided in the request body as an `InvitationRequest`. 
    The invitation is saved in the database and the user will receive it.

    Args:
        invitation (InvitationRequest): The details of the invitation to send.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        None: No content is returned, but the invitation is created.
    """
    await create_invitation(db, invitation)
    return None


@router.post("/invitations/accept/{invitation_id}", status_code=status.HTTP_201_CREATED)
async def accept_invitation(invitation_id: int, db: AsyncSession = Depends(get_db)):
    """Accept an invitation.

    This endpoint allows a user to accept a specific invitation using its unique ID. 
    Once accepted, the invitation is processed, and the user is added to the home, 
    team, or other related entities as defined by the invitation.

    Args:
        invitation_id (int): The ID of the invitation to accept.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        None: No content is returned, but the invitation is accepted and processed.
    """
    await take_up_invitation(db, invitation_id)
    return None


@router.delete(
    "/invitations/decline/{invitation_id}", status_code=status.HTTP_204_NO_CONTENT
)
async def decline_invitation(invitation_id: int, db: AsyncSession = Depends(get_db)):
    """Decline an invitation.

    This endpoint allows a user to decline a specific invitation by its unique ID. 
    Once declined, the invitation is removed from the system.

    Args:
        invitation_id (int): The ID of the invitation to decline.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: A successful response indicating the invitation was declined.
    """
    await reject_invitation(db, invitation_id)
    return None
