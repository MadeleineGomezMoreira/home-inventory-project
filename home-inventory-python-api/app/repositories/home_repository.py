from app.models.models import Home
from sqlalchemy.ext.asyncio import AsyncSession

# TODO: implement error handling


# Save a new home
async def save_home(session: AsyncSession, home: Home):
    session.add(home)
    session.commit()
    return home


# Update an existing home (works the same as save)
async def update_home(session: AsyncSession, home: Home):
    session.add(home)
    await session.commit()


# Find all homes
async def get_all_homes(session: AsyncSession):
    return session.query(Home).all()


# Find all homes by owner
async def get_all_homes_by_owner(session: AsyncSession, owner_id: int):
    return session.query(Home).filter_by(owned_by=owner_id).all()


# Find a home by home_id
async def get_home_by_id(session: AsyncSession, home_id: int):
    return session.query(Home).filter_by(id=home_id).first()


# Find a home by owner (owned_by -> user.id)
async def get_home_by_owner(session: AsyncSession, owner_id: int):
    return session.query(Home).filter_by(owned_by=owner_id).first()
