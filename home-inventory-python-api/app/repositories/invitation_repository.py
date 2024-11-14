from app.models.models import Invitation, UserHome, UserRole
from app.repositories.user_repository import get_user_by_username
from app.mappers.invitation_mapper import map_invitationRequest_to_invitation
from app.exceptions.custom_exceptions import (
    UserNotFoundError,
    InvitationNotFoundError,
    UserAlreadyInHomeException,
    InvitationAlreadyExistsException,
)
from app.schemas.invitation import InvitationRequest
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select


# Send an invitation
async def create_invitation(
    session: AsyncSession, invitation_request: InvitationRequest
):
    username = invitation_request.invitee_username
    user = await get_user_by_username(session, username)

    if user is None:
        raise UserNotFoundError("User not found")
    else:
        member_role = UserRole.MEMBER.value

        # Check if user is already in home
        stmt = select(UserHome).where(
            UserHome.home_id == invitation_request.home_id,
            UserHome.user_id == user.id,
            UserHome.role == member_role,
        )

        result = await session.execute(stmt)

        if result.scalar() is not None:
            raise UserAlreadyInHomeException(
                "The user you are trying to invite is already in the home"
            )

        ## Check if there is already an invitation for this user to join the home
        existing_invitation_stmt = select(Invitation).where(
            Invitation.home_id == invitation_request.home_id,
            Invitation.invitee_id == user.id,
        )

        existing_invitation_result = await session.execute(existing_invitation_stmt)
        if existing_invitation_result.scalar():
            raise InvitationAlreadyExistsException(
                "An invitation already exists for this user to join the home"
            )

        # If all checks pass, add the invitation to the db
        mapped_invitation = map_invitationRequest_to_invitation(
            invitation_request, user.id
        )
        session.add(mapped_invitation)
        await session.commit()
        await session.refresh(mapped_invitation)


# Reject an invitation
async def reject_invitation(session: AsyncSession, invitation_id: int):
    # Execute a delete statement where the invitation_id matches
    stmt = delete(Invitation).where(Invitation.id == invitation_id)
    result = await session.execute(stmt)

    # Check if any rows were deleted
    if result.rowcount == 0:
        raise InvitationNotFoundError("The invitation was not found")

    await session.commit()


# Accept an invitation
async def take_up_invitation(session: AsyncSession, invitation_id: int):
    async with session.begin():
        # First, retrieve the invitation from the database
        invitation = await session.get(Invitation, invitation_id)

        if invitation is None:
            # Raise an error if the invitation doesn't exist
            raise InvitationNotFoundError("The invitation was not found")

        # Check if the user is already a member of the home to avoid duplicate entries
        member_role = UserRole.MEMBER.value
        stmt = select(UserHome).where(
            UserHome.user_id == invitation.invitee_id,
            UserHome.home_id == invitation.home_id,
            UserHome.role == member_role,
        )
        result = await session.execute(stmt)
        existing_user_home = result.scalar_one_or_none()

        if existing_user_home:
            raise UserAlreadyInHomeException("User is already a member of this home")

        # If no existing entry, create a new UserHome record for the invitee
        user_home = UserHome(
            user_id=invitation.invitee_id, home_id=invitation.home_id, role=member_role
        )
        session.add(user_home)
        await session.commit()

        # Finally, delete the invitation from the database
        await session.delete(invitation)
        await session.commit()
