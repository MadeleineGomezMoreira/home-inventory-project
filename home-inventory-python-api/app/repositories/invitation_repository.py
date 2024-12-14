from app.models.models import Invitation, UserHome, UserRole, Home
from app.repositories.user_repository import get_user_by_username
from app.mappers.invitation_mapper import map_invitationRequest_to_invitation
from app.exceptions.custom_exceptions import (
    HomeNotFoundException,
    InvitationAcceptingException,
    UserNotFoundException,
    InvitationNotFoundException,
    UserAlreadyInHomeException,
    InvitationAlreadyExistsException,
)
from app.schemas.invitation import InvitationRequest, InvitationInfoResponse
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from sqlalchemy.orm import selectinload


async def get_received_invitations(session: AsyncSession, user_id: int):
    """
    Retrieve all invitations that a specific user has received.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        user_id (int): The ID of the user whose invitations are being retrieved.

    Returns:
        list[`Invitation`]: A list of invitations received by the user.
    """
    stmt = select(Invitation).where(Invitation.invitee_id == user_id)
    result = await session.execute(stmt)
    return result.scalars().all()


async def create_invitation(
    session: AsyncSession, invitation_request: InvitationRequest
):
    """
    Send an invitation to a user to join a home.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        invitation_request (`InvitationRequest`): The data for the invitation.

    Raises:
        `UserNotFoundException`: If the specified user does not exist.
        `UserAlreadyInHomeException`: If the user is already part of the home as a member.
        `InvitationAlreadyExistsException`: If the user has already been invited to the home.
    """
    username = invitation_request.invitee_username
    user = await get_user_by_username(session, username)

    if not user:
        raise UserNotFoundException()
    else:
        member_role = UserRole.MEMBER.value

        stmt = select(UserHome).where(
            UserHome.home_id == invitation_request.home_id,
            UserHome.user_id == user.id,
            UserHome.role == member_role,
        )

        result = await session.execute(stmt)

        if result.scalar() is not None:
            raise UserAlreadyInHomeException()

        existing_invitation_stmt = select(Invitation).where(
            Invitation.home_id == invitation_request.home_id,
            Invitation.invitee_id == user.id,
        )

        existing_invitation_result = await session.execute(existing_invitation_stmt)
        if existing_invitation_result.scalar():
            raise InvitationAlreadyExistsException()

        mapped_invitation = map_invitationRequest_to_invitation(
            invitation_request, user.id
        )
        session.add(mapped_invitation)
        await session.commit()
        await session.refresh(mapped_invitation)


async def reject_invitation(session: AsyncSession, invitation_id: int):
    """
    Reject and delete an existing invitation.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        invitation_id (`int`): The ID of the invitation to reject and delete.

    Raises:
        `InvitationNotFoundException`: If the invitation with the specified ID does not exist.
    """
    stmt = delete(Invitation).where(Invitation.id == invitation_id)
    result = await session.execute(stmt)

    if result.rowcount == 0:
        raise InvitationNotFoundException()

    await session.commit()

async def get_invitation_info(session: AsyncSession, invitation_id: int):
    """
    Retrieve detailed information about an invitation.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        invitation_id (`int`): The ID of the invitation whose details are to be retrieved.

    Raises:
        `InvitationNotFoundException`: If the invitation does not exist.
        `HomeNotFoundException`: If the home associated with the invitation is not found.

    Returns:
        `InvitationInfoResponse`: The detailed information about the invitation.
    """
    invitation_res = await session.execute(
        select(Invitation)
        .options(selectinload(Invitation.inviter))
        .where(Invitation.id == invitation_id)
        )
    
    invitation: Invitation = invitation_res.scalar_one_or_none()
    
    if not invitation:
        raise InvitationNotFoundException()

    home_res = await session.execute(
        select(Home)
        .where(Home.id==invitation.home_id)
    )

    home: Home = home_res.scalar_one_or_none()
    
    if not home:
        raise HomeNotFoundException()

    inviter_username = invitation.inviter.username
    home_name = home.home_name

    return InvitationInfoResponse(id = invitation_id ,inviter_name = inviter_username, home_name= home_name)


async def take_up_invitation(session: AsyncSession, invitation_id: int):
    """
    Accept an invitation to join a home and add the user as a member.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        invitation_id (`int`): The ID of the invitation to accept.

    Raises:
        `InvitationNotFoundException`: If the invitation does not exist.
        `UserAlreadyInHomeException`: If the user is already a member of the home.
    """
    async with session.begin():
        try:
            invitation = await session.get(Invitation, invitation_id)

            if not invitation:
                raise InvitationNotFoundException()

            member_role = UserRole.MEMBER.value
            stmt = select(UserHome).where(
                UserHome.user_id == invitation.invitee_id,
                UserHome.home_id == invitation.home_id,
                UserHome.role == member_role,
            )
            result = await session.execute(stmt)
            existing_user_home = result.scalar_one_or_none()

            if existing_user_home:
                raise UserAlreadyInHomeException()

            user_home = UserHome(
                user_id=invitation.invitee_id, home_id=invitation.home_id, role=member_role
            )
            session.add(user_home)

            await session.delete(invitation)
            await session.commit()
        except Exception as e:
            await session.rollback()
            raise InvitationAcceptingException(e) from e
            