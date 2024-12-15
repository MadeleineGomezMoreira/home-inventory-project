from email.policy import HTTP
from fastapi import APIRouter, Depends, status
from app.schemas.item import ItemMoveRequest, ItemRequest, ItemResponse, ItemCreate, ItemResponseNoTags
from app.database.db_engine import get_db
from app.repositories.item_repository import (
    delete_item,
    move_item,
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
    """Retrieve a specific item by its ID.

    This endpoint fetches details of a specific item from the database using its unique 
    `item_id` and returns the item details as an `ItemResponse`.

    Args:
        item_id (int): The ID of the item to retrieve.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        ItemResponse: A response containing the item details.
    """
    item = await get_item(db, item_id)
    return ItemResponse.model_validate(item, from_attributes=True)


@router.get("/items/route/{item_id}")
async def get_item_route(item_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve the route of a specific item.

    This endpoint fetches the route of the specified item based on its `item_id`.
    The route typically refers to the item's location or path within a system.

    Args:
        item_id (int): The ID of the item to retrieve the route for.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        str: A string representing the item's route or location.
    """
    item_route = await get_route(db, item_id)
    return item_route

@router.put("/items/move/")
async def move_single_item(itemMoveRequest: ItemMoveRequest, db: AsyncSession = Depends(get_db)):
    """Move an item to a different location.

    This endpoint moves an item from its current location to a new location, as specified 
    in the `ItemMoveRequest`. The request contains details about the item and the new location.

    Args:
        itemMoveRequest (ItemMoveRequest): The details of the item to move and its new location.
        db (AsyncSession): The database session dependency injected by FastAPI.
    """
    await move_item(db, itemMoveRequest)

@router.get(
    "/items/comp/{comp_id}",
    response_model=list[ItemResponseNoTags],
)
async def read_all_items_in_compartment(
    comp_id: int, db: AsyncSession = Depends(get_db)
):
    """Retrieve all items within a specific compartment.

    This endpoint retrieves a list of all items located within the specified compartment 
    identified by `comp_id`. The items are returned as a list of `ItemResponseNoTags`, 
    which excludes tags information.

    Args:
        comp_id (int): The ID of the compartment whose items are being fetched.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[ItemResponseNoTags]: A list of items within the specified compartment.
        list[]: An empty list given the result contains no 'Item' objects.
    """
    items = await get_items_from_compartment(db, comp_id)
    return [
        ItemResponseNoTags.model_validate(item, from_attributes=True) for item in items
    ]


@router.put("/items/", response_model=ItemResponse)
async def update_single_item(
    updated_item: ItemRequest, db: AsyncSession = Depends(get_db)
):
    """Update an existing item.

    This endpoint allows updating an item using the provided `ItemRequest`. The request 
    contains the updated details of the item, and the item is updated in the database.

    Args:
        updated_item (ItemRequest): The details of the item to update.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        ItemResponse: A response containing the updated item details.
    """
    updated_item = await update_item(db, updated_item)
    return ItemResponse.model_validate(updated_item, from_attributes=True)


@router.post(
    "/items/", status_code=status.HTTP_201_CREATED, response_model=ItemResponse
)
async def create_item(item: ItemCreate, db: AsyncSession = Depends(get_db)):
    """Create a new item.

    This endpoint allows the creation of a new item in the system. The item details are 
    provided in the request body as an `ItemCreate` object. Upon successful creation, 
    the newly created item is returned.

    Args:
        item (ItemCreate): The details of the item to create.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        ItemResponse: A response containing the newly created item details.
    """
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
    """Search for items within a specific home.

    This endpoint searches for items within a specific home, based on the provided 
    `home_id` and `search_word`. The items matching the search criteria are returned 
    as a list of `ItemResponseNoTags`, excluding tags information.

    Args:
        home_id (int): The ID of the home to search for items within.
        search_word (str): The search keyword used to filter the items.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[ItemResponseNoTags]: A list of items matching the search criteria within the specified home.
    """
    items = await get_items_by_string(db, search_word, home_id)
    return [
        ItemResponseNoTags.model_validate(item, from_attributes=True) for item in items
    ]


@router.delete("/items/{item_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_item(item_id: int, db: AsyncSession = Depends(get_db)):
    """Delete a specific item.

    This endpoint deletes an item from the database using its unique `item_id`. Once 
    deleted, the item is permanently removed from the system.

    Args:
        item_id (int): The ID of the item to delete.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: A successful response indicating the item was deleted.
    """
    await delete_item(db, item_id)
