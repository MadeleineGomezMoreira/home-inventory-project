from app.models.models import Home, UserHome, UserRole, Room, Invitation, Furniture
from app.schemas.home import HomeCreate, HomeRequest, HomeResponse, HomesByRoleResponse
from app.mappers.home_mapper import map_homeCreate_to_home
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from app.exceptions.custom_exceptions import HomeNotFoundError, UserNotInHomeException

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

    session.add(user_home)
    await session.commit()
    await session.refresh(mapped_home)
    await session.refresh(user_home)

    return mapped_home


# Join a home as a member
async def join_home_as_member(session: AsyncSession, user_id: int, home_id: int):
    user_home = UserHome(user_id=user_id, home_id=home_id, role=UserRole.MEMBER.value)
    session.add(user_home)
    await session.commit()

    return None


# Update an existing home
async def update_home(session: AsyncSession, home_request: HomeRequest):
    existing_home = await session.get(Home, home_request.id)

    if existing_home is None:
        raise HomeNotFoundError("Home was not found")

    existing_home.home_name = home_request.home_name

    await session.commit()
    await session.refresh(existing_home)

    return existing_home


# Delete an existing home
async def delete_home(session: AsyncSession, home_id: int):
    async with session.begin():
        home = await session.get(Home, home_id)

        if home is None:
            raise HomeNotFoundError("Home was not found")

        # TODO: change this whenever I actually include compartments and items here

        # Find all rooms in the home
        rooms = await session.execute(select(Room.id).where(Room.home_id == home_id))
        room_ids = [row.id for row in rooms.scalars()]

        # Delete furniture associated with the rooms
        await session.execute(delete(Furniture).where(Furniture.room_id.in_(room_ids)))
        await session.execute(delete(Room).where(Room.home_id == home_id))
        await session.execute(delete(Invitation).where(Invitation.home_id == home_id))
        await session.execute(delete(UserHome).where(UserHome.home_id == home_id))
        await session.execute(delete(Home).where(Home.id == home_id))


# Remove a user from a home
async def remove_user_from_home(session: AsyncSession, home_id: int, user_id: int):
    stmt = select(UserHome).where(
        UserHome.user_id == user_id,
        UserHome.home_id == home_id,
        UserHome.role == UserRole.MEMBER.value,
    )

    result = await session.execute(stmt)
    user_home = result.scalar_one_or_none()

    if not user_home:
        raise UserNotInHomeException("User is not a member of the specified home.")

    # Delete the UserHome record if found
    await session.delete(user_home)
    await session.commit()


# Find all homes
async def get_all_homes(session: AsyncSession):
    result = await session.execute(select(Home))
    return result.scalars().all()


# Find all homes by user (users_homes table)
async def get_all_homes_by_user_by_role(session: AsyncSession, user_id: int):
    stmt = (
        select(Home, UserHome.role)
        .join(UserHome)
        .filter(
            UserHome.user_id == user_id,
        )
    )
    result = await session.execute(stmt)

    homes_by_role = {"OWNER": [], "MEMBER": []}

    for home, role in result.all():
        home_response = HomeResponse(
            id=home.id,
            home_name=home.home_name,
            owned_by=home.owned_by,
        )
        homes_by_role[role].append(home_response)

    print(homes_by_role)

    return HomesByRoleResponse(
        OWNER=homes_by_role["OWNER"], MEMBER=homes_by_role["MEMBER"]
    )


# Find a home by home_id
async def get_home_by_id(session: AsyncSession, home_id: int):
    stmt = select(Home).where(Home.id == home_id)
    result = await session.execute(stmt)
    return result.scalars().first()
