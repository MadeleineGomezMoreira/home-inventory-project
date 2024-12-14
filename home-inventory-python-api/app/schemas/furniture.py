from pydantic import BaseModel, ConfigDict
from app.utils.camel_case_generator import camel_case_generator


class FurnitureBase(BaseModel):
    """Base model for Furniture data.

    This model serves as the base class for the Furniture-related models and contains 
    common attributes. It is meant to be inherited by other models related to furniture.

    Attributes:
        furn_name (`str`): The name of the furniture item.
        room_id (`int`): The ID of the room where the furniture is located.

    Model Configuration:
        - from_attributes (`bool`): Enables reading attributes from the model for serialization.
    """
    furn_name: str
    room_id: int

    model_config = ConfigDict(from_attributes=True)


class FurnitureCreate(FurnitureBase):
    """Model used for creating a new furniture item.

    This model inherits from FurnitureBase and specifies additional configuration
    for creating a new furniture item. It includes alias generation for field names
    to follow camelCase style for JSON serialization.

    Attributes:
        furn_name (`str`): The name of the furniture item.
        room_id (`int`): The ID of the room where the furniture is located.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    furn_name: str
    room_id: int

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class FurnitureResponse(FurnitureBase):
    """Response model for furniture data.

    This model is used for returning furniture data in API responses. It inherits from
    FurnitureBase and includes an `id` field that uniquely identifies the furniture item.

    Attributes:
        id (`int`): The unique ID of the furniture item.
        furn_name (`str`): The name of the furniture item.
        room_id (`int`): The ID of the room where the furniture is located.

    Model Configuration:
        - from_attributes (`bool`): Enables reading attributes from the model for serialization.
        - extra (`str`): Ignores any extra fields not defined in the model.
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    furn_name: str
    room_id: int

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class FurnitureRequest(BaseModel):
    """Request model for furniture data.

    This model is used for updating or modifying existing furniture data. It contains
    only the necessary fields for handling the request and excludes additional fields 
    like `room_id`.

    Attributes:
        id (`int`): The unique ID of the furniture item.
        furn_name (`str`): The name of the furniture item.

    Model Configuration:
        - from_attributes (`bool`): Enables reading attributes from the model for serialization.
        - extra (`str`): Ignores any extra fields not defined in the model.
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    furn_name: str

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
