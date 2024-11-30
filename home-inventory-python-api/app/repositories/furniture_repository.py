from app.models.models import Furniture, Compartment
from app.schemas import furniture
from app.schemas.furniture import FurnitureCreate, FurnitureRequest
from app.mappers.furniture_mapper import map_furniture_create_to_furniture
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from app.exceptions.custom_exceptions import FurnitureNotFoundError


# Save a new piece of furniture
async def save_furniture(session: AsyncSession, furniture: FurnitureCreate):
    mapped_furniture = map_furniture_create_to_furniture(furniture)
    session.add(mapped_furniture)
    await session.commit()
    await session.refresh(mapped_furniture)

    return mapped_furniture


# Retrieve all the furniture from a specific room
async def get_furniture_from_room(session: AsyncSession, room_id: int):
    stmt = select(Furniture).where(Furniture.room_id == room_id)
    result = await session.execute(stmt)
    return result.scalars().all()


# Retrieve a piece of furniture by its id
async def get_furniture_by_id(session: AsyncSession, furn_id: int):
    stmt = select(Furniture).where(Furniture.id == furn_id)
    result = await session.execute(stmt)
    furniture = result.scalar_one_or_none()
    print(furniture)
    return furniture


# Delete a piece of furniture
async def delete_furniture(session: AsyncSession, furn_id: int):
    async with session.begin():
        furniture = await session.get(Furniture, furn_id)

        if furniture is None:
            raise FurnitureNotFoundError(
                "The piece of furniture was not found in the room"
            )

        # Delete the piece of furniture
        await session.execute(delete(Furniture).where(Furniture.id == furn_id))


# Update a piece of furniture
async def update_furniture(session: AsyncSession, furniture_request=FurnitureRequest):
    existing_furniture = await session.get(Furniture, furniture_request.id)

    if existing_furniture is None:
        raise FurnitureNotFoundError(
            f"Furniture with ID: {furniture_request.id} was not found"
        )

    existing_furniture.furn_name = furniture_request.furn_name

    await session.commit()
    await session.refresh(existing_furniture)

    return existing_furniture
