from pydantic import BaseModel, ConfigDict
from typing import List


class CompartmentBase(BaseModel):
    comp_name: str
    furn_id: int

    model_config = ConfigDict(from_attributes=True)


class CompartmentCreate(CompartmentBase):

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class CompartmentResponse(CompartmentBase):
    id: int
    comp_name: str
    furn_id: int

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class CompartmentRequest(BaseModel):
    id: int
    comp_name: str

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )
