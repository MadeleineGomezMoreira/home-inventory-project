from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession, async_sessionmaker
from sqlalchemy.orm import DeclarativeBase
from app.models.base import Base
from app.schemas.home import HomeCreate
import pytest

DATABASE_URL = "sqlite+aiosqlite:///:memory:"


@pytest.fixture(scope="module")
async def async_engine():
    engine = create_async_engine(
        DATABASE_URL, connect_args={"check_same_thread": False}, echo=True
    )
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
    yield engine
    await engine.dispose()


@pytest.fixture(scope="function")
async def async_session(async_engine):
    async_session_maker = async_sessionmaker(bind=async_engine, expire_on_commit=False)
    async with async_session_maker() as session:
        yield session


@pytest.fixture
def sample_home():
    return HomeCreate(home_name="Test Home", owner_id=1)
