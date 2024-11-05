from fastapi import APIRouter, Depends, status
from app.schemas.home import HomeResponse, HomeCreate, HomesByRoleResponse
from app.database.db_engine import get_db
from app.repositories.home_repository import (
    get_all_homes,
    get_all_homes_by_user_by_role,
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


@router.get("/homes/single/{home_id}", response_model=HomeResponse)
async def get_home(home_id: int, db: AsyncSession = Depends(get_db)):
    home = await get_home_by_id(db, home_id)
    return HomeResponse.model_validate(home, from_attributes=True)


@router.post(
    "/homes/", status_code=status.HTTP_201_CREATED, response_model=HomeResponse
)
async def create_home(home: HomeCreate, db: AsyncSession = Depends(get_db)):
    created_home = await save_home(db, home)
    return HomeResponse.model_validate(created_home, from_attributes=True)


@router.get("/homes/{user_id}", response_model=HomesByRoleResponse)
async def read_all_homes_by_user_by_role(
    user_id: int, db: AsyncSession = Depends(get_db)
):
    homes = await get_all_homes_by_user_by_role(db, user_id)
    return homes
