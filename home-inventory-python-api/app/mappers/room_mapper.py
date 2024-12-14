from app.models.models import Room
from app.schemas.room import RoomCreate


def map_roomCreate_to_room(room_create: RoomCreate) -> Room:
    """Maps a RoomCreate schema object to a Room model object.

    This function converts data from the `RoomCreate` Pydantic schema, which is used for creating new rooms,
    into a `Room` model object that can be used for database operations, typically during the process of adding 
    a new room to the system.

    Args:
        room_create (RoomCreate): The input schema object containing the details for the room to be created.

    Returns:
        Room: The `Room` model populated with the data from the `RoomCreate` schema.
    """
    return Room(room_name=room_create.room_name, home_id=room_create.home_id)
