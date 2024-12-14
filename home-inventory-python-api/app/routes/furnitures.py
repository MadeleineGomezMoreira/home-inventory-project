from fastapi import APIRouter, Depends, status
from app.database.db_engine import get_db
from sqlalchemy.ext.asyncio import AsyncSession
from app.repositories.furniture_repository import (
    delete_furniture,
    save_furniture,
    get_furniture_by_id,
    get_furniture_from_room,
    update_furniture,
)
from app.schemas import furniture
from app.schemas.furniture import FurnitureCreate, FurnitureRequest, FurnitureResponse

router = APIRouter()


@router.get("/furniture/{furn_id}", response_model=FurnitureResponse)
async def get_single_furniture(furn_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve a single piece of furniture by its ID.

    This endpoint fetches a specific piece of furniture from the database based on the 
    provided furniture ID and returns it as a `FurnitureResponse`.

    Args:
        furn_id (int): The ID of the furniture to retrieve.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        FurnitureResponse: The furniture object with the specified ID.
    """
    furniture = await get_furniture_by_id(db, furn_id)
    return FurnitureResponse.model_validate(furniture, from_attributes=True)


@router.post(
    "/furniture/", status_code=status.HTTP_201_CREATED, response_model=FurnitureResponse
)
async def create_furniture(
    furniture: FurnitureCreate, db: AsyncSession = Depends(get_db)
):
    """Create a new piece of furniture.

    This endpoint allows creating a new piece of furniture by providing the necessary 
    details in the request body as a `FurnitureCreate` model. The newly created furniture 
    is returned in the response.

    Args:
        furniture (FurnitureCreate): The details of the new piece of furniture to create.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        FurnitureResponse: The created furniture details.
    """
    created_furniture = await save_furniture(db, furniture)
    return FurnitureResponse.model_validate(created_furniture, from_attributes=True)


@router.get("/furniture/room/{room_id}", response_model=list[FurnitureResponse])
async def get_furniture(room_id: int, db: AsyncSession = Depends(get_db)):
    """Get all pieces of furniture in a specific room.

    This endpoint retrieves all furniture associated with a specific room ID. It returns 
    a list of `FurnitureResponse` models.

    Args:
        room_id (int): The ID of the room to fetch furniture for.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[FurnitureResponse]: A list of furniture items in the specified room.
    """
    furniture_results = await get_furniture_from_room(db, room_id)
    return [
        FurnitureResponse.model_validate(furniture, from_attributes=True)
        for furniture in furniture_results
    ]


@router.put("/furniture/", response_model=FurnitureResponse)
async def update_single_furniture(
    furniture_request: FurnitureRequest, db: AsyncSession = Depends(get_db)
):
    """Update an existing piece of furniture.

    This endpoint allows updating the details of an existing piece of furniture. The updated 
    furniture details are passed in the request body as a `FurnitureRequest` model, and the 
    updated furniture is returned in the response.

    Args:
        furniture_request (FurnitureRequest): The updated details of the furniture.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        FurnitureResponse: The updated furniture details.
    """
    updated_furniture = await update_furniture(db, furniture_request)
    return FurnitureResponse.model_validate(updated_furniture, from_attributes=True)


@router.delete("/furniture/{furniture_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_furniture(
    furniture_id: int, db: AsyncSession = Depends(get_db)
):
    """Delete a piece of furniture by its ID.

    This endpoint deletes a piece of furniture by its unique ID. It removes the specified 
    furniture from the database.

    Args:
        furniture_id (int): The ID of the furniture to delete.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: A successful response indicating the resource was deleted.
    """
    await delete_furniture(db, furniture_id)
