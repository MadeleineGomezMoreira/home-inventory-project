from app.models.models import Invitation
from app.schemas.invitation import InvitationRequest


def map_invitationRequest_to_invitation(
    invitation_request: InvitationRequest, invited_id: int
) -> Invitation:
    """Maps an InvitationRequest schema object to an Invitation model object.

    This function converts data from the `InvitationRequest` Pydantic schema, which is used for invitation creation,
    into an `Invitation` model object that can be used for database operations, typically during the process of creating 
    an invitation for a user to join a home.

    Args:
        invitation_request (InvitationRequest): The input schema object containing the invitation details.
        invited_id (int): The ID of the user being invited to the home (the invitee).

    Returns:
        Invitation: The `Invitation` model populated with the data from the `InvitationRequest` schema and the invited user's ID.
    """
    return Invitation(
        inviter_id=invitation_request.inviter_id,
        invitee_id=invited_id,
        home_id=invitation_request.home_id,
    )
