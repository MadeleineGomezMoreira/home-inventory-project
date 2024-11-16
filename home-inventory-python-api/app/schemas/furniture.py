from pydantic import BaseModel, ConfigDict


class FurnitureBase(BaseModel):
    furn_name: str
    room_id: int

    model_config = ConfigDict(from_attributes=True)


class FurnitureCreate(FurnitureBase):
    furn_name: str
    room_id: int

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class FurnitureResponse(FurnitureBase):
    id: int
    furn_name: str
    room_id: int

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class FurnitureRequest(BaseModel):
    id: int
    furn_name: str

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )
