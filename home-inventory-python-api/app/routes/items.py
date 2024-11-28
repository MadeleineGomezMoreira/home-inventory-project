from email.policy import HTTP
from fastapi import APIRouter, Depends, status
from app.schemas.item import ItemRequest, ItemResponse, ItemCreate, ItemResponseNoTags
from app.database.db_engine import get_db
from app.repositories.item_repository import (
    delete_item,
    save_item,
    get_items_by_string,
    get_items_from_compartment,
    get_item,
    update_item,
    delete_item,
    get_route,
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()


@router.get("/items/{item_id}", response_model=ItemResponse)
async def read_item(item_id: int, db: AsyncSession = Depends(get_db)):
    item = await get_item(db, item_id)
    return ItemResponse.model_validate(item, from_attributes=True)


@router.get("/items/route/{item_id}")
async def get_item_route(item_id: int, db: AsyncSession = Depends(get_db)):
    item_route = await get_route(db, item_id)
    return item_route


@router.get(
    "/items/comp/{comp_id}",
    response_model=list[ItemResponseNoTags],
)
async def read_all_items_in_compartment(
    comp_id: int, db: AsyncSession = Depends(get_db)
):
    items = await get_items_from_compartment(db, comp_id)
    return [
        ItemResponseNoTags.model_validate(item, from_attributes=True) for item in items
    ]


@router.put("/items/", response_model=ItemResponse)
async def update_single_item(
    updated_item: ItemRequest, db: AsyncSession = Depends(get_db)
):
    updated_item = await update_item(db, updated_item)
    return ItemResponse.model_validate(updated_item, from_attributes=True)


@router.post(
    "/items/", status_code=status.HTTP_201_CREATED, response_model=ItemResponse
)
async def create_item(item: ItemCreate, db: AsyncSession = Depends(get_db)):
    created_item = await save_item(db, item)
    return ItemResponse.model_validate(created_item, from_attributes=True)


@router.get(
    "/items/home/{home_id}",
    response_model=list[ItemResponseNoTags],
    status_code=status.HTTP_200_OK,
)
async def search_items_in_home(
    home_id: int,
    search_word: str,
    db: AsyncSession = Depends(get_db),
):
    items = await get_items_by_string(db, search_word, home_id)
    return [
        ItemResponseNoTags.model_validate(item, from_attributes=True) for item in items
    ]


@router.delete("/items/{item_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_item(item_id: int, db: AsyncSession = Depends(get_db)):
    await delete_item(db, item_id)
