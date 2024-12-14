from pydantic import BaseModel, ConfigDict
from app.utils.camel_case_generator import camel_case_generator


class InvitationBase(BaseModel):
    """Base model for Invitation data.

    This model serves as the base class for all invitation-related models and contains
    the common attributes related to invitations. It is meant to be inherited by other 
    invitation-related models.

    Attributes:
        id (`int`): The unique identifier for the invitation.
        inviter_id (`int`): The ID of the user who is sending the invitation.
        invitee_id (`int`): The ID of the user who is receiving the invitation.
        home_id (`int`): The ID of the home associated with the invitation.
    """
    id: int
    inviter_id: int
    invitee_id: int
    home_id: int

class InvitationInfoResponse(BaseModel):
    """Response model for invitation information.

    This model is used to return detailed information about an invitation in an API response. 
    It includes the `inviter_name` and `home_name` associated with the invitation.

    Attributes:
        id (`int`): The unique identifier for the invitation.
        inviter_name (`str`): The name of the inviter.
        home_name (`str`): The name of the home associated with the invitation.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    inviter_name: str
    home_name: str
    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class InvitationRequest(BaseModel):
    """Request model for creating an invitation.

    This model is used for creating new invitations, where the inviter sends an invite to the invitee 
    for a specific home. It includes the `inviter_id`, `invitee_username`, and `home_id`.

    Attributes:
        inviter_id (`int`): The ID of the user who is sending the invitation.
        invitee_username (`str`): The username of the user who is receiving the invitation.
        home_id (`int`): The ID of the home associated with the invitation.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    inviter_id: int
    invitee_username: str
    home_id: int
    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class InvitationResponse(InvitationBase):
    """Response model for an invitation.

    This model is used to return the full details of an invitation in API responses. 
    It inherits from the `InvitationBase` model and includes the common invitation attributes.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
