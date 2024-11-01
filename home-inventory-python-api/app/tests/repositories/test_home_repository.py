import pytest
from app.repositories.home_repository import (
    save_home,
    update_home,
    get_all_homes,
    get_all_homes_by_owner,
    get_home_by_id,
    get_home_by_name_and_owner,
)
from app.schemas.home import HomeCreate
from sqlalchemy.ext.asyncio import AsyncSession


# Prueba para guardar un nuevo hogar
@pytest.mark.asyncio
async def test_save_home(async_session: AsyncSession, sample_home: HomeCreate):
    new_home = await save_home(async_session, sample_home)
    assert new_home.home_name == "Test Home"
    assert new_home.owned_by == sample_home.owner_id
