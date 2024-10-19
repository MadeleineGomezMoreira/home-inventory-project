from app.models.models import User
from app.schemas.user import UserCreate
from app.mappers.user_mapper import map_userCreate_to_user
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select


# Save a new user
async def save_user(session: AsyncSession, user: UserCreate):
    mapped_user = map_userCreate_to_user(user)
    session.add(mapped_user)
    await session.commit()
    await session.refresh(mapped_user)
    return mapped_user


# Delete an existing user by user.id
async def delete_user(session: AsyncSession, user_id: int):
    user = session.query(User).filter_by(id=user_id).first()

    if user:
        session.delete(user)
        await session.commit()


# Update an existing user (works the same as save)
async def update_user(session: AsyncSession, user: User):
    session.add(user)
    await session.commit()


# Find all users
async def get_all_users(session: AsyncSession):

    result = await session.execute(select(User))
    return result.scalars().all()


# TODO:change this to use select instead of query
# Find a user by user_id
async def get_user_by_id(session: AsyncSession, user_id: int):
    return session.query(User).filter_by(id=user_id).first()


# Find a user by username
async def get_user_by_username(session: AsyncSession, username: str):
    return session.query(User).filter_by(username=username).first()
