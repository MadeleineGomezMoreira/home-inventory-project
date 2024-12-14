from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker
from app.models.base import Base
#from app.database.config import DATABASE_URL
from sqlalchemy import text
import os

DATABASE_URL = os.getenv('DATABASE_URL', "mysql+aiomysql://root:quevedo2dam@dam2.mysql.iesquevedo.es:3335/madegomez_TFG")


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
