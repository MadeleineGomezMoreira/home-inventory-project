from app.models.models import Room
from sqlalchemy.ext.asyncio import AsyncSession
from app.schemas.room import RoomCreate, RoomRequest
from app.mappers.room_mapper import map_roomCreate_to_room
from app.exceptions.custom_exceptions import RoomNotFoundError, SavingRoomError
from sqlalchemy import delete, select
from sqlalchemy.exc import SQLAlchemyError
from app.exceptions.custom_exceptions import RoomNotFoundError


# Save a new room
async def save_room(session: AsyncSession, room: RoomCreate):
    try:
        mapped_room = map_roomCreate_to_room(room)
        session.add(mapped_room)
        await session.commit()
        await session.refresh(mapped_room)

        return mapped_room
    except SQLAlchemyError as e:
        await session.rollback()
        raise SavingRoomError(f"Failed to save room: {e}") from e


# Update an existent room
async def update_room(session: AsyncSession, room_request: RoomRequest):
    room = await session.get(Room, room_request.id)
    if room:
        # Update the room with the new values
        room.room_name = room_request.room_name
        room.home_id = room_request.home_id
        await session.commit()
        await session.refresh(room)
        return room
    else:
        raise RoomNotFoundError("The room was not found")


# Delete an existing room
async def delete_room(session: AsyncSession, room_id: int):
    async with session.begin():
        # Retrieve the room object
        room = await session.get(Room, room_id)

        if room is None:
            raise RoomNotFoundError("The room was not found")

        # Delete the room
        await session.delete(room)


# See all rooms in a home
async def get_rooms_in_home(session: AsyncSession, id: int):
    stmt = select(Room).where(Room.home_id == id)
    result = await session.execute(stmt)
    return result.scalars().all()
