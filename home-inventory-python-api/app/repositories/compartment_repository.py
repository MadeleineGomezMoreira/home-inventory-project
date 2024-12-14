from app.models.models import Compartment
from app.schemas.compartment import CompartmentCreate, CompartmentRequest
from app.mappers.compartment_mapper import map_compartmentCreate_to_Compartment
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from app.exceptions.custom_exceptions import (
    CompartmentNotFoundException, 
    FurnitureNotFoundException, 
    SavingCompartmentException,
    CompartmentUpdatingException,
    )

async def get_compartments(session: AsyncSession, furn_id: int):
    """
    Retrieve all compartments for a specific piece of furniture.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        furn_id (`int`): The ID of the furniture whose compartments are to be retrieved.

    Raises:
        `FurnitureNotFoundException`: if the furniture ID does not match any inside of the database.

    Returns:
        list[`Compartment`]: A list of compartments belonging to the specified furniture.
    """
    try:
        stmt = select(Compartment).where(Compartment.furn_id == furn_id)
        result = await session.execute(stmt)
        return result.scalars().all()
    except Exception as e:
        raise FurnitureNotFoundException(e) from e

async def get_compartment(session: AsyncSession, comp_id: int):
    """
    Retrieve a single compartment by its unique ID.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        comp_id (`int`): The ID of the compartment to be retrieved.

    Raises:
        `CompartmentNotFoundException`: If no compartment exists with the given ID.

    Returns:
        `Compartment`: The compartment object corresponding to the provided ID.
    """
    stmt = select(Compartment).where(Compartment.id == comp_id)
    result = await session.execute(stmt)
    compartment = result.scalar_one_or_none()
    if not compartment:
        raise CompartmentNotFoundException()
    return compartment


async def save_compartment(session: AsyncSession, compartment: CompartmentCreate):
    """
    Save a new compartment to the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        compartment (`CompartmentCreate`): The data for the new compartment to be created.

    Raises:
        `SavingCompartmentException`: If an error occurs while saving the compartment.

    Returns:
        `Compartment`: The newly created compartment object.
    """
    mapped_compartment = map_compartmentCreate_to_Compartment(compartment)
    try:
        session.add(mapped_compartment)
        await session.commit()
        await session.refresh(mapped_compartment)
        return mapped_compartment
    
    except Exception as e:
        await session.rollback()
        raise SavingCompartmentException(e) from e


# Update a compartment
async def update_compartment(
    session: AsyncSession, compartment_request: CompartmentRequest
):
    """
    Update an existing compartment in the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        compartment_request (`CompartmentRequest`): The data for the compartment update.

    Raises:
        `CompartmentNotFoundException`: If the specified compartment ID does not exist.
        `CompartmentUpdatingException`: If an error occurs during the update process.

    Returns:
        `Compartment`: The updated compartment object.
    """
    try: 
        existing_compartment = await session.get(Compartment, compartment_request.id)

        if not existing_compartment:
            raise CompartmentNotFoundException()

        existing_compartment.comp_name = compartment_request.comp_name

        await session.commit()
        await session.refresh(existing_compartment)

        return existing_compartment
    except Exception as e:
        raise CompartmentUpdatingException(e) from e


async def delete_compartment(session: AsyncSession, comp_id: int):
    """
    Delete a compartment from the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        comp_id (`int`): The ID of the compartment to be deleted.

    Raises:
        `CompartmentNotFoundException`: If the specified compartment ID does not exist.
    """
    async with session.begin():
        compartment = await session.get(Compartment, comp_id)

        if not compartment:
            raise CompartmentNotFoundException()

        await session.execute(delete(Compartment).where(Compartment.id == comp_id))
