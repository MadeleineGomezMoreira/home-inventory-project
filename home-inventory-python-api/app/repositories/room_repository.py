from app.models.models import Room
from sqlalchemy.ext.asyncio import AsyncSession
from app.schemas.room import RoomCreate, RoomRequest
from app.mappers.room_mapper import map_roomCreate_to_room
from app.exceptions.custom_exceptions import (
    RoomNotFoundException,
    RoomUpdatingException, 
    SavingRoomException
) 
from sqlalchemy import select
from sqlalchemy.exc import SQLAlchemyError


async def get_room(session: AsyncSession, room_id: int):
    """
    Retrieve a room by its ID.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        room_id (`int`): The ID of the room to retrieve.

    Raises:
        `RoomNotFoundException`: If no room exists with the given ID.

    Returns:
        `Room`: The room object corresponding to the provided ID.
    """
    stmt = select(Room).where(Room.id == room_id)
    result = await session.execute(stmt)
    room = result.scalar_one_or_none()
    if not room:
        raise RoomNotFoundException()
    return room

async def save_room(session: AsyncSession, room: RoomCreate):
    """
    Save a new room to the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        room (`RoomCreate`): The data for the new room to be created.

    Raises:
        `SavingRoomException`: If an error occurs while saving the room.

    Returns:
        `Room`: The newly created room object.
    """
    try:
        mapped_room = map_roomCreate_to_room(room)
        session.add(mapped_room)
        await session.commit()
        await session.refresh(mapped_room)

        return mapped_room
    except SQLAlchemyError as e:
        await session.rollback()
        raise SavingRoomException(e)


async def update_room(session: AsyncSession, room_request: RoomRequest):
    """
    Update an existing room with new data.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        room_request (`RoomRequest`): The data for updating the room.

    Raises:
        `RoomNotFoundException`: If no room exists with the given ID.
        `RoomUpdatingException`: If an error occurs while updating the room.

    Returns:
        `Room`: The updated room object.
    """
    room = await session.get(Room, room_request.id)
    if not room:
        raise RoomNotFoundException()
    try:
        room.room_name = room_request.room_name
        room.home_id = room_request.home_id
        await session.commit()
        await session.refresh(room)
        return room
    except Exception as e:
        raise RoomUpdatingException() from e


async def delete_room(session: AsyncSession, room_id: int):
    """
    Delete an existing room from the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        room_id (`int`): The ID of the room to delete.

    Raises:
        `RoomNotFoundException`: If no room exists with the given ID.

    Returns:
        None
    """
    async with session.begin():
        room = await session.get(Room, room_id)

        if not room:
            raise RoomNotFoundException()

        await session.delete(room)


async def get_rooms_in_home(session: AsyncSession, id: int):
    """
    Retrieve all rooms associated with a specific home.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        id (`int`): The ID of the home to retrieve rooms for.

    Returns:
        list[`Room`]: A list of room objects associated with the given home.
    """
    stmt = select(Room).where(Room.home_id == id)
    result = await session.execute(stmt)
    return result.scalars().all()
