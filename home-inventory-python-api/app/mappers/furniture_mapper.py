from app.models.models import Furniture
from app.schemas.furniture import FurnitureCreate


def map_furniture_create_to_furniture(furniture_create: FurnitureCreate) -> Furniture:
    return Furniture(
        furn_name=furniture_create.furn_name, room_id=furniture_create.room_id
    )
