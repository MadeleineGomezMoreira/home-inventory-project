from app.models.models import Compartment
from app.schemas.compartment import CompartmentCreate, CompartmentRequest
from app.mappers.compartment_mapper import map_compartmentCreate_to_Compartment
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from app.exceptions.custom_exceptions import CompartmentNotFoundError, RoomNotFoundError


# Retrieve all compartments from a piece of furniture
async def get_compartments(session: AsyncSession, furn_id: int):
    stmt = select(Compartment).where(Compartment.furn_id == furn_id)
    result = await session.execute(stmt)
    return result.scalars().all()


# Save a new compartment
async def save_compartment(session: AsyncSession, compartment: CompartmentCreate):
    mapped_compartment = map_compartmentCreate_to_Compartment(compartment)
    session.add(mapped_compartment)
    await session.commit()
    await session.refresh(mapped_compartment)

    return mapped_compartment


# Update a compartment
async def update_compartment(
    session: AsyncSession, compartment_request: CompartmentRequest
):
    existing_compartment = await session.get(Compartment, compartment_request.id)

    if existing_compartment is None:
        raise CompartmentNotFoundError(
            f"Compartment with ID: {compartment_request.id} was not found"
        )

    existing_compartment.comp_name = compartment_request.comp_name

    await session.commit()
    await session.refresh(existing_compartment)

    return existing_compartment


# Delete a compartment (and in the future all of its items)
async def delete_compartment(session: AsyncSession, comp_id: int):
    async with session.begin():
        compartment = await session.get(Compartment, comp_id)

        if compartment is None:
            raise CompartmentNotFoundError(
                "The compartment was not found in the piece of furniture"
            )

        # TODO: change this whenever I actually include items inside of the compartments

        await session.execute(delete(Compartment).where(Compartment.id == comp_id))
