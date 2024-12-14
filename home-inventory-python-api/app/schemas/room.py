from pydantic import BaseModel, ConfigDict
from app.utils.camel_case_generator import camel_case_generator


class RoomResponse(BaseModel):
    """Response model for room data.

    This model is used to return detailed information about a room, including 
    the room's `id`, `room_name`, and the associated `home_id`. This model is 
    typically used when responding to API requests that retrieve information 
    about rooms.

    Attributes:
        id (`int`): The unique identifier for the room.
        room_name (`str`): The name of the room.
        home_id (`int`): The ID of the home to which the room belongs.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - extra (`str`): Specifies how to handle extra fields not defined in the model. In this case, `extra="ignore"` means extra fields will be ignored.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    room_name: str
    home_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        extra="ignore",
        alias_generator=camel_case_generator,
    )


class RoomCreate(BaseModel):
    """Request model for creating a new room.

    This model is used for creating a new room. It includes the `room_name` 
    and `home_id`, which are required for creating a new room within a specific home.

    Attributes:
        room_name (`str`): The name of the room to be created.
        home_id (`int`): The ID of the home to which the room will belong.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    room_name: str
    home_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class RoomRequest(RoomResponse):
    """Request model for updating or modifying a room.

    This model extends `RoomResponse` and is used when updating or modifying 
    room information. It inherits fields from `RoomResponse` and can be used 
    to update room details.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
