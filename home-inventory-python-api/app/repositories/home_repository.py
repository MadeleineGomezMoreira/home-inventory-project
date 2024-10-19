from app.models.models import Home
from app.schemas.home import HomeCreate
from app.mappers.home_mapper import map_homeCreate_to_home
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

# TODO: implement error handling


# Save a new home
async def save_home(session: AsyncSession, home: HomeCreate):
    mapped_home = map_homeCreate_to_home(home)
    session.add(mapped_home)
    await session.commit()
    await session.refresh(mapped_home)
    return mapped_home


# Update an existing home (works the same as save)
async def update_home(session: AsyncSession, home: HomeCreate):
    mapped_home = map_homeCreate_to_home(home)
    session.add(mapped_home)
    await session.commit()


# Find all homes
async def get_all_homes(session: AsyncSession):
    result = await session.execute(select(Home))
    return result.scalars().all()


# Find all homes by owner
async def get_all_homes_by_owner(session: AsyncSession, user_id: int):
    stmt = select(Home).where(Home.owned_by == user_id)
    result = await session.execute(stmt)
    return result.scalars().all()


# Find a home by home_id
async def get_home_by_id(session: AsyncSession, home_id: int):
    stmt = select(Home).where(Home.id == home_id)
    result = await session.execute(stmt)
    return result.scalars().first()


# Find a home by home_name and owner_id (owned_by -> owner_id)
async def get_home_by_name_and_owner(
    session: AsyncSession, home_name: str, user_id: int
):
    stmt = select(Home).where(Home.home_name == home_name & Home.owned_by == user_id)
    result = await session.execute(stmt)
    return result.scalars().first()
