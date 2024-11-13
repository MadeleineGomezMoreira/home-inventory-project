from app.repositories.user_repository import get_user_by_username
from app.mappers.invitation_mapper import map_invitationRequest_to_invitation
from app.exceptions.custom_exceptions import UserNotFoundError
from app.schemas.invitation import InvitationRequest
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select


async def create_invitation(
    session: AsyncSession, invitation_request: InvitationRequest
):
    username = invitation_request.invitee_username
    user = await get_user_by_username(session, username)

    if user is None:
        raise UserNotFoundError("User not found")
    else:
        mapped_invitation = map_invitationRequest_to_invitation(
            invitation_request, user.id
        )
        session.add(mapped_invitation)
        await session.commit()
        await session.refresh(mapped_invitation)
