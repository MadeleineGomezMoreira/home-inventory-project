from pydantic import BaseModel, ConfigDict


class InvitationBase(BaseModel):
    id: int
    inviter_id: int
    invitee_id: int
    home_id: int


class InvitationRequest(BaseModel):
    inviter_id: int
    invitee_username: str
    home_id: int
    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class InvitationResponse(InvitationBase):
    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )
