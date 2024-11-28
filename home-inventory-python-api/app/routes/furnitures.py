from fastapi import APIRouter, Depends, status
from app.database.db_engine import get_db
from sqlalchemy.ext.asyncio import AsyncSession
from app.repositories.furniture_repository import (
    delete_furniture,
    save_furniture,
    get_furniture,
    get_furniture_from_room,
    update_furniture,
)
from app.schemas import furniture
from app.schemas.furniture import FurnitureCreate, FurnitureRequest, FurnitureResponse

router = APIRouter()


@router.get("/furniture/{furn_id}", response_model=FurnitureResponse)
async def get_single_furniture(furnId: int, db: AsyncSession = Depends(get_db)):
    furniture = await get_furniture(db, furnId)
    return FurnitureResponse.model_validate(furniture, from_attributes=True)


@router.post(
    "/furniture/", status_code=status.HTTP_201_CREATED, response_model=FurnitureResponse
)
async def create_furniture(
    furniture: FurnitureCreate, db: AsyncSession = Depends(get_db)
):
    created_furniture = await save_furniture(db, furniture)
    return FurnitureResponse.model_validate(created_furniture, from_attributes=True)


@router.get("/furniture/room/{room_id}", response_model=list[FurnitureResponse])
async def get_furniture(room_id: int, db: AsyncSession = Depends(get_db)):
    furniture_results = await get_furniture_from_room(db, room_id)
    return [
        FurnitureResponse.model_validate(furniture, from_attributes=True)
        for furniture in furniture_results
    ]


@router.put("/furniture/", response_model=FurnitureResponse)
async def update_single_furniture(
    furniture_request: FurnitureRequest, db: AsyncSession = Depends(get_db)
):
    updated_furniture = await update_furniture(db, furniture_request)
    return FurnitureResponse.model_validate(updated_furniture, from_attributes=True)


@router.delete("/furniture/{furniture_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_furniture(
    furniture_id: int, db: AsyncSession = Depends(get_db)
):
    await delete_furniture(db, furniture_id)
