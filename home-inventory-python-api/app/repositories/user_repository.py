from app.models.models import (
    User,
    UserHome,
    Furniture,
    Invitation,
    Home,
    Room,
    Compartment,
)
from app.schemas.user import UserCreate, UserResponse, UsersByRoleResponse
from app.mappers.user_mapper import map_userCreate_to_user
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, delete
from app.exceptions.custom_exceptions import UserNotFoundError

# TODO: implement error handling


# Save a new user
async def save_user(session: AsyncSession, user: UserCreate):
    mapped_user = map_userCreate_to_user(user)
    session.add(mapped_user)
    await session.commit()
    await session.refresh(mapped_user)
    return mapped_user


# Get all users from a home grouped by role
async def get_all_users_by_home_by_role(session: AsyncSession, home_id: int):
    stmt = (
        select(User, UserHome.role)
        .join(UserHome)
        .filter(
            UserHome.home_id == home_id,
        )
    )
    result = await session.execute(stmt)

    users_by_role = {"OWNER": None, "MEMBER": []}

    for user, role in result.all():
        user_response = UserResponse(
            id=user.id,
            username=user.username,
            email=user.email,
        )
        if role == "OWNER":
            users_by_role["OWNER"] = user_response
        elif role == "MEMBER":
            users_by_role["MEMBER"].append(user_response)

    print(users_by_role)

    return UsersByRoleResponse(
        OWNER=users_by_role["OWNER"], MEMBER=users_by_role["MEMBER"]
    )


# Delete an existing user by user.id, including furniture and compartments
async def delete_user(session: AsyncSession, user_id: int):
    # Fetch the user to delete
    user = await session.get(User, user_id)

    if user is None:
        raise UserNotFoundError(f"User with ID {user_id} not found")

    # Delete the user
    await session.execute(delete(User).where(User.id == user_id))


# Update an existing user (works the same as save)
async def update_user(session: AsyncSession, user: UserCreate):
    mapped_user = map_userCreate_to_user(user)
    session.add(mapped_user)
    await session.commit()


# Find all users
async def get_all_users(session: AsyncSession):
    result = await session.execute(select(User))
    return result.scalars().all()


# Find a user by user_id
async def get_user_by_id(session: AsyncSession, user_id: int):
    stmt = select(User).where(User.id == user_id)
    result = await session.execute(stmt)
    return result.scalars().first()


# Find a user by username
async def get_user_by_username(session: AsyncSession, username: str):
    stmt = select(User).where(User.username == username)
    result = await session.execute(stmt)
    return result.scalars().first()
