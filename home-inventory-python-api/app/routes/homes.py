from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from app.schemas.home import HomeResponse, HomeCreate
from app.database.db_engine import get_db
from app.repositories.home_repository import (
    get_all_homes,
    get_all_homes_by_owner,
    get_home_by_name_and_owner,
    get_home_by_id,
    save_home,
    update_home,
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()


@router.get("/homes/", response_model=list[HomeResponse])
async def read_all_homes(db: AsyncSession = Depends(get_db)):
    homes = await get_all_homes(db)
    return [HomeResponse.model_validate(home, from_attributes=True) for home in homes]


@router.post(
    "/homes/", status_code=status.HTTP_201_CREATED, response_model=HomeResponse
)
async def create_home(home: HomeCreate, db: AsyncSession = Depends(get_db)):
    created_home = await save_home(db, home)
    return HomeResponse.model_validate(created_home, from_attributes=True)
