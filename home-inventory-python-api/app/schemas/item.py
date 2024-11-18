from typing import List, Optional
from pydantic import BaseModel, ConfigDict


class ItemBase(BaseModel):
    item_name: str
    comp_id: int


class TagResponse(BaseModel):
    id: int
    tag_name: str
    home_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class ItemCreate(ItemBase):
    tags: Optional[List[str]] = None

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class ItemRequest(ItemBase):
    id: int
    tags: Optional[List[TagResponse]] = None

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )


class ItemResponse(ItemBase):
    id: int
    tags: List[TagResponse] = []

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )
