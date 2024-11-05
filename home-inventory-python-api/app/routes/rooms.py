from fastapi import APIRouter, Depends, HTTPException, status
from app.schemas.room import RoomResponse, RoomCreate
from app.database.db_engine import get_db
from app.repositories.room_repository import get_rooms_in_home, save_room
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()


@router.get("/rooms/home/{home_id}", response_model=list[RoomResponse])
async def read_all_rooms_in_home(home_id: int, db: AsyncSession = Depends(get_db)):
    rooms = await get_rooms_in_home(db, home_id)
    return [RoomResponse.model_validate(room, from_attributes=True) for room in rooms]


@router.post(
    "/rooms/", status_code=status.HTTP_201_CREATED, response_model=RoomResponse
)
async def create_room(room: RoomCreate, db: AsyncSession = Depends(get_db)):
    created_room = await save_room(db, room)
    return RoomResponse.model_validate(created_room, from_attributes=True)
