from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker
from app.database.config import DATABASE_URL
from app.models.base import Base
from sqlalchemy import text


engine = create_async_engine(
    DATABASE_URL,
    echo=True,
)
SessionLocal = async_sessionmaker(engine)

# This will get the DB Session for each request


async def get_db():
    async with engine.begin() as connection:
        await connection.run_sync(Base.metadata.create_all)

    db = SessionLocal()
    try:
        yield db
    finally:
        await db.close()
