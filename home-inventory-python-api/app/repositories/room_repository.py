from app.models.models import Room
from sqlalchemy.ext.asyncio import AsyncSession
from app.schemas.room import RoomCreate
from app.mappers.room_mapper import map_roomCreate_to_room
from sqlalchemy import select


# Save a new room
async def save_room(session: AsyncSession, room: RoomCreate):
    mapped_room = map_roomCreate_to_room(room)
    session.add(mapped_room)
    await session.commit()
    await session.refresh(mapped_room)

    return mapped_room


# See all rooms in a home
async def get_rooms_in_home(session: AsyncSession, id: int):
    stmt = select(Room).where(Room.home_id == id)
    result = await session.execute(stmt)
    return result.scalars().all()
