from app.models.models import Invitation
from app.schemas.invitation import InvitationRequest


def map_invitationRequest_to_invitation(
    invitation_request: InvitationRequest, invited_id: int
) -> Invitation:
    return Invitation(
        inviter_id=invitation_request.inviter_id,
        invitee_id=invited_id,
        home_id=invitation_request.home_id,
    )
