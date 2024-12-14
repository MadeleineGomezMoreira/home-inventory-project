from app.models.models import Furniture
from app.schemas.furniture import FurnitureCreate


def map_furniture_create_to_furniture(furniture_create: FurnitureCreate) -> Furniture:
    """Maps a FurnitureCreate schema object to a Furniture model object.

    This function converts data from the `FurnitureCreate` Pydantic schema, which is used for data validation 
    and handling user input, into a `Furniture` model object that can be used for database operations.

    Args:
        furniture_create (FurnitureCreate): The input schema object containing the furniture data.

    Returns:
        Furniture: The `Furniture` model populated with the data from the `FurnitureCreate` schema.
    """
    return Furniture(
        furn_name=furniture_create.furn_name, room_id=furniture_create.room_id
    )
