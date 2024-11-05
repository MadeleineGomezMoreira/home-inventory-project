from pydantic import BaseModel, ConfigDict


class RoomResponse(BaseModel):
    id: int
    room_name: str
    home_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        extra="ignore",
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class RoomCreate(BaseModel):
    room_name: str
    home_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )
