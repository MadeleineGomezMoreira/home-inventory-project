from app.models.models import User, UserHome
from app.schemas.user import UserCreate, UserResponse
from app.mappers.user_mapper import map_userCreate_to_user
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select

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
        .joim(UserHome)
        .filter(
            UserHome.home_id == home_id,
        )
    )
    result = await session.execute(stmt)

    users_by_role = {"OWNER": [], "MEMBER": []}

    for user, role in result.all():
        users_by_role = UserResponse(
            id=user.id,
            username=user.username,
            email=user.email,
        )
        users_by_role[role].append()

    print(users_by_role)

    return UsersByRoleResponse(
        OWNER=users_by_role["OWNER"], MEMBER=users_by_role["MEMBER"]
    )


# Delete an existing user by user.id
async def delete_user(session: AsyncSession, user_id: int):
    user = session.query(User).filter_by(id=user_id).first()

    if user:
        session.delete(user)
        await session.commit()


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
