from email.policy import HTTP
from fastapi import APIRouter, Depends, status
from app.schemas.item import ItemRequest, ItemResponse, ItemCreate
from app.database.db_engine import get_db
from app.repositories.item_repository import (
    save_item,
    get_items_by_string,
    get_items_from_compartment,
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()


@router.get(
    "/items/{comp_id}",
    response_model=list[ItemResponse],
)
async def read_all_items_in_compartment(
    comp_id: int, db: AsyncSession = Depends(get_db)
):
    items = await get_items_from_compartment(db, comp_id)
    return [ItemResponse.model_validate(item, from_attributes=True) for item in items]


@router.post(
    "/items/", status_code=status.HTTP_201_CREATED, response_model=ItemResponse
)
async def create_item(item: ItemCreate, db: AsyncSession = Depends(get_db)):
    created_item = await save_item(db, item)
    return ItemResponse.model_validate(created_item, from_attributes=True)


@router.get(
    "/items/home/{home_id}",
    response_model=list[ItemResponse],
    status_code=status.HTTP_200_OK,
)
async def search_items_in_home(
    home_id: int,
    search_word: str,
    db: AsyncSession = Depends(get_db),
):
    items = await get_items_by_string(db, search_word, home_id)
    return [ItemResponse.model_validate(item, from_attributes=True) for item in items]
