from app.models.models import Furniture, Compartment
from app.schemas import furniture
from app.schemas.furniture import FurnitureCreate, FurnitureRequest
from app.mappers.furniture_mapper import map_furniture_create_to_furniture
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from app.exceptions.custom_exceptions import (
    FurnitureNotFoundException, 
    SavingFurnitureException,
    FurnitureUpdatingException,
)

async def save_furniture(session: AsyncSession, furniture: FurnitureCreate):
    """
    Save a new piece of furniture to the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        furniture (`FurnitureCreate`): The data for the new furniture to be created.

    Raises:
        `SavingFurnitureException`: If an error occurs while saving the furniture.

    Returns:
        `Furniture`: The newly created furniture object.
    """
    try: 
        mapped_furniture = map_furniture_create_to_furniture(furniture)
        session.add(mapped_furniture)
        await session.commit()
        await session.refresh(mapped_furniture)

        return mapped_furniture
    except Exception as e:
        raise SavingFurnitureException(e) from e


async def get_furniture_from_room(session: AsyncSession, room_id: int):
    """
    Retrieve all furniture associated with a specific room.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        room_id (`int`): The ID of the room whose furniture is to be retrieved.

    Returns:
        list[`Furniture`]: A list of furniture objects belonging to the specified room.
    """
    stmt = select(Furniture).where(Furniture.room_id == room_id)
    result = await session.execute(stmt)
    return result.scalars().all()


async def get_furniture_by_id(session: AsyncSession, furn_id: int):
    """
    Retrieve a single piece of furniture by its associated ID.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        furn_id (`int`): The ID of the furniture to be retrieved.

    Raises:
        `FurnitureNotFoundException`: If no furniture exists with the given ID.

    Returns:
        `Furniture`: The furniture object corresponding to the provided ID.
    """
    stmt = select(Furniture).where(Furniture.id == furn_id)
    result = await session.execute(stmt)
    furniture = result.scalar_one_or_none()
    if not furniture:
        raise FurnitureNotFoundException()
    return furniture


async def delete_furniture(session: AsyncSession, furn_id: int):
    """
    Delete a piece of furniture from the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        furn_id (`int`): The ID of the furniture to be deleted.

    Raises:
        `FurnitureNotFoundException`: If the specified furniture ID does not exist.
    """
    async with session.begin():
        furniture = await session.get(Furniture, furn_id)

        if not furniture:
            raise FurnitureNotFoundException()

        await session.execute(delete(Furniture).where(Furniture.id == furn_id))


async def update_furniture(session: AsyncSession, furniture_request=FurnitureRequest):
    """
    Update an existing piece of furniture in the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        furniture_request (`FurnitureRequest`): The data for the furniture update.

    Raises:
        `FurnitureNotFoundException`: If the specified furniture ID does not exist.
        `FurnitureUpdatingException`: If an error occurs during the update process.

    Returns:
        `Furniture`: The updated furniture object.
    """
    existing_furniture = await session.get(Furniture, furniture_request.id)

    if not existing_furniture:
        raise FurnitureNotFoundException()

    try: 
        existing_furniture.furn_name = furniture_request.furn_name

        await session.commit()
        await session.refresh(existing_furniture)

        return existing_furniture
    except Exception as e:
        await session.rollback()
        raise FurnitureUpdatingException(e) from e
        
