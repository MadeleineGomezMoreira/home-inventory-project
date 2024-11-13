from fastapi import APIRouter, Depends, status
from app.schemas.invitation import InvitationResponse, InvitationRequest
from app.database.db_engine import get_db
from app.repositories.invitation_repository import create_invitation
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()


@router.post("/invitations/send/", status_code=status.HTTP_201_CREATED)
async def send_invitation(
    invitation: InvitationRequest, db: AsyncSession = Depends(get_db)
):
    await create_invitation(db, invitation)
    return None
