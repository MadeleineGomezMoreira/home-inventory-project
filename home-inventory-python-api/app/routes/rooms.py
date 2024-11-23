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
    room = await get_room(db, room_id)
    return RoomResponse.model_validate(room, from_attributes=True)


@router.get("/rooms/home/{home_id}", response_model=list[RoomResponse])
async def read_all_rooms_in_home(home_id: int, db: AsyncSession = Depends(get_db)):
    rooms = await get_rooms_in_home(db, home_id)
    return [RoomResponse.model_validate(room, from_attributes=True) for room in rooms]


@router.put("/rooms/", response_model=RoomResponse)
async def update_given_room(room: RoomRequest, db: AsyncSession = Depends(get_db)):
    updated_room = await update_room(db, room)
    return RoomResponse.model_validate(updated_room, from_attributes=True)


@router.delete("/rooms/{room_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_room(room_id: int, db: AsyncSession = Depends(get_db)):
    await delete_room(db, room_id)


@router.post(
    "/rooms/", status_code=status.HTTP_201_CREATED, response_model=RoomResponse
)
async def create_room(room: RoomCreate, db: AsyncSession = Depends(get_db)):
    created_room = await save_room(db, room)
    return RoomResponse.model_validate(created_room, from_attributes=True)
