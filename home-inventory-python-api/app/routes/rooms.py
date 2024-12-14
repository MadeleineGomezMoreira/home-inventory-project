from fastapi import APIRouter, Depends, HTTPException, status
from app.schemas.room import RoomRequest, RoomResponse, RoomCreate
from app.database.db_engine import get_db
from app.repositories.room_repository import (
    delete_room,
    get_rooms_in_home,
    save_room,
    update_room,
    delete_room,
    get_room,
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()


@router.get("/rooms/{room_id}", response_model=RoomResponse)
async def get_single_room(room_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve a specific room by its ID.

    This endpoint retrieves a room from the database using its unique `room_id`.
    The room details are returned in the `RoomResponse` format.

    Args:
        room_id (int): The ID of the room to retrieve.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        RoomResponse: A response containing the room details.
    """
    room = await get_room(db, room_id)
    return RoomResponse.model_validate(room, from_attributes=True)


@router.get("/rooms/home/{home_id}", response_model=list[RoomResponse])
async def read_all_rooms_in_home(home_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve all rooms within a specific home.

    This endpoint retrieves a list of all rooms that belong to the home specified
    by `home_id`. The rooms are returned as a list of `RoomResponse` objects.

    Args:
        home_id (int): The ID of the home whose rooms are being fetched.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[RoomResponse]: A list of rooms within the specified home.
    """
    rooms = await get_rooms_in_home(db, home_id)
    return [RoomResponse.model_validate(room, from_attributes=True) for room in rooms]


@router.put("/rooms/", response_model=RoomResponse)
async def update_given_room(room: RoomRequest, db: AsyncSession = Depends(get_db)):
    """Update the details of a specific room.

    This endpoint updates a room's details based on the provided `RoomRequest` 
    object. The request includes the new room data, and the updated room is returned 
    in the `RoomResponse` format.

    Args:
        room (RoomRequest): The details of the room to update.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        RoomResponse: A response containing the updated room details.
    """
    updated_room = await update_room(db, room)
    return RoomResponse.model_validate(updated_room, from_attributes=True)


@router.delete("/rooms/{room_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_room(room_id: int, db: AsyncSession = Depends(get_db)):
    """Delete a specific room by its ID.

    This endpoint deletes the room identified by `room_id` from the database. 
    Once deleted, the room is permanently removed.

    Args:
        room_id (int): The ID of the room to delete.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: A successful response indicating the room was deleted.
    """
    await delete_room(db, room_id)


@router.post(
    "/rooms/", status_code=status.HTTP_201_CREATED, response_model=RoomResponse
)
async def create_room(room: RoomCreate, db: AsyncSession = Depends(get_db)):
    """Create a new room.

    This endpoint creates a new room in the database based on the provided `RoomCreate` 
    request. Upon successful creation, the details of the newly created room are returned 
    in the `RoomResponse` format.

    Args:
        room (RoomCreate): The details of the room to create.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        RoomResponse: A response containing the newly created room details.
    """
    created_room = await save_room(db, room)
    return RoomResponse.model_validate(created_room, from_attributes=True)
