from app.models.models import Compartment
from app.schemas.compartment import CompartmentCreate


def map_compartmentCreate_to_Compartment(
    compartment_create: CompartmentCreate,
) -> Compartment:
    return Compartment(
        comp_name=compartment_create.comp_name, furn_id=compartment_create.furn_id
    )
