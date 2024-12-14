from app.models.models import (
    Home,
    UserHome,
    UserRole,
)
from app.schemas.home import HomeCreate, HomeRequest, HomeResponse, HomesByRoleResponse
from app.mappers.home_mapper import map_homeCreate_to_home
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import delete, select
from app.exceptions.custom_exceptions import (
    HomeDeletionException,
    HomeNotFoundException,
    HomeUpdatingException,
    MemberRemovalException,
    SavingHomeException,
    UserNotFoundException, 
    UserNotInHomeException
    )

async def get_is_user_owner(session: AsyncSession, user_id: int, home_id: int):
    """
    Retrieve a boolean that shows if a user is the owner of a specific home.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        user_id (`int`): The ID of the user to check ownership.
        home_id (`int`): The ID of the home to check ownership for.

    Raises:
        `UserNotFoundException`: If the user is not found in the specified home.

    Returns:
        `bool`: True if the user is the owner of the home, False otherwise.
    """
    stmt = select(UserHome.role).where(
        UserHome.user_id == user_id,
        UserHome.home_id == home_id,
    )

    result = await session.execute(stmt)

    role: UserRole = result.scalar_one_or_none()
    
    if not role:
        raise UserNotFoundException()
    
    is_owner = role == 'OWNER'
    return is_owner

async def save_home(session: AsyncSession, home: HomeCreate):
    """
    Save a new home and assign the owner.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        home (`HomeCreate`): The data for the new home to be created.

    Raises:
        `SavingHomeException`: If an error occurs while saving the home.

    Returns:
        `Home`: The newly created home object.
    """
    try:
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
    except Exception as e:
        raise SavingHomeException(e) from e


async def join_home_as_member(session: AsyncSession, user_id: int, home_id: int):
    """
    Add a user as a member to a specific home.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        user_id (`int`): The ID of the user to be added as a member.
        home_id (`int`): The ID of the home to add the user to.
    """
    user_home = UserHome(user_id=user_id, home_id=home_id, role=UserRole.MEMBER.value)
    session.add(user_home)
    await session.commit()

    return None


async def update_home(session: AsyncSession, home_request: HomeRequest):
    """
    Update an existing home.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        home_request (`HomeRequest`): The data for the home update.

    Raises:
        `HomeNotFoundException`: If the home is not found.
        `HomeUpdatingException`: If an error occurs while updating the home.

    Returns:
        `Home`: The updated home object.
    """
    try: 
        existing_home = await session.get(Home, home_request.id)

        if not existing_home:
            raise HomeNotFoundException()

        existing_home.home_name = home_request.home_name

        await session.commit()
        await session.refresh(existing_home)

        return existing_home
    except Exception as e:
        await session.rollback()
        raise HomeUpdatingException(e) from e
    
async def delete_home(session: AsyncSession, home_id: int):
    """
    Delete an existing home and its associated data.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        home_id (`int`): The ID of the home to be deleted.

    Raises:
        `HomeNotFoundException`: If the specified home is not found.
        `HomeDeletionException`: If an error occurs during the deletion process.
    """
    try:
        home = await session.get(Home, home_id)

        if home is None:
            raise HomeNotFoundException("Home was not found")
        
        await session.execute(delete(Home).where(Home.id == home_id))
        await session.commit()
    except Exception as e:
        await session.rollback()
        raise HomeDeletionException(e) from e


async def remove_user_from_home(session: AsyncSession, home_id: int, user_id: int):
    """
    Remove a user from a home.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        home_id (`int`): The ID of the home from which the user will be removed.
        user_id (`int`): The ID of the user to be removed from the home.

    Raises:
        `UserNotInHomeException`: If the user is not part of the home.
        `MemberRemovalException`: If an error occurs while removing the user.
    """
    try:
        stmt = select(UserHome).where(
            UserHome.user_id == user_id,
            UserHome.home_id == home_id,
            UserHome.role == UserRole.MEMBER.value,
        )

        result = await session.execute(stmt)
        user_home = result.scalar_one_or_none()

        if not user_home:
            raise UserNotInHomeException()

        await session.delete(user_home)
        await session.commit()
    except Exception as e:
        await session.rollback()
        raise MemberRemovalException(e) from e


async def get_all_homes(session: AsyncSession):
    """
    Retrieve all homes from the database.

    Args:
        session (`AsyncSession`): The database session used to execute the query.

    Returns:
        list[`Home`]: A list of all home objects.
    """
    result = await session.execute(select(Home))
    return result.scalars().all()


async def get_all_homes_by_user_by_role(session: AsyncSession, user_id: int):
    """
    Retrieve all homes a user is associated with, categorized by role.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        user_id (`int`): The ID of the user whose homes are to be retrieved.

    Returns:
        `HomesByRoleResponse`: A response containing lists of homes by role (OWNER, MEMBER).
    """
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

    return HomesByRoleResponse(
        OWNER=homes_by_role["OWNER"], MEMBER=homes_by_role["MEMBER"]
    )


async def get_home_by_id(session: AsyncSession, home_id: int):
    """
    Retrieve a home by its ID.

    Args:
        session (`AsyncSession`): The database session used to execute the query.
        home_id (`int`): The ID of the home to be retrieved.

    Raises:
        `HomeNotFoundException`: If no home exists with the given ID.

    Returns:
        `Home`: The home object corresponding to the provided ID.
    """
    stmt = select(Home).where(Home.id == home_id)
    result = await session.execute(stmt)
    home = result.scalar_one_or_none()
    if not home:
        raise HomeNotFoundException()
    return home
