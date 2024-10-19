from pydantic import BaseModel, ConfigDict, Field
from typing import List


class HomeBase(BaseModel):
    home_name: str
    owned_by: int

    model_config = ConfigDict(from_attributes=True)


class HomeCreate(HomeBase):
    home_name: str
    owned_by: int

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class HomeResponse(HomeBase):
    id: int
    home_name: str
    owned_by: int

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
    )
