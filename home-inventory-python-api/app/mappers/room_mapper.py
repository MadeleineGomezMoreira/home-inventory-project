from app.models.models import Room
from app.schemas.room import RoomCreate


def map_roomCreate_to_room(room_create: RoomCreate) -> Room:

    return Room(room_name=room_create.room_name, home_id=room_create.home_id)
