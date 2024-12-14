from app.models.models import Compartment
from app.schemas.compartment import CompartmentCreate


def map_compartmentCreate_to_Compartment(
    compartment_create: CompartmentCreate,
) -> Compartment:
    """Maps a CompartmentCreate schema object to a Compartment model object.

    This function is used to convert the data from the `CompartmentCreate` Pydantic schema, which is used for data 
    validation and input handling, into a `Compartment` model, which is used for database interactions.

    Args:
        compartment_create (CompartmentCreate): The input schema object containing the compartment data.
        
    Returns:
        Compartment: The `Compartment` model populated with the data from the `CompartmentCreate` schema.
    """
    return Compartment(
        comp_name=compartment_create.comp_name, furn_id=compartment_create.furn_id
    )
