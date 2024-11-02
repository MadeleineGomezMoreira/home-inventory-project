from inspect import _void
from app.models.models import Home, UserHome, UserRole
from app.routes import users
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

    user_home = UserHome(
        user_id=mapped_home.owned_by, home_id=mapped_home.id, role=UserRole.OWNER.value
    )

    print(f"user_home-ROLE: {user_home.role}")

    session.add(user_home)
    await session.commit()
    await session.refresh(mapped_home)
    await session.refresh(user_home)

    return mapped_home


# Join a home as a member
async def join_home_as_member(session: AsyncSession, user_id: int, home_id: int):
    user_home = UserHome(user_id=user_id, home_id=home_id, role=UserRole.MEMBER)
    session.add(user_home)
    await session.commit()

    return None


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


# Find all homes by user (users_homes table)
async def get_all_homes_by_user_by_role(session: AsyncSession, user_id: int):
    stmt = (
        select(Home)
        .join(UserHome)
        .filter(
            UserHome.user_id == user_id,
        )
    )
    result = await session.execute(stmt)

    homes_by_role = {"OWNER": [], "MEMBER": []}

    for home, role in result.scalars().all():
        homes_by_role[role].append(home)

    return homes_by_role


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
