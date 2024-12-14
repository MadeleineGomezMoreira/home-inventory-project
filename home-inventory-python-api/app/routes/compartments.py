from fastapi import APIRouter, Depends, status
from app.database.db_engine import get_db
from sqlalchemy.ext.asyncio import AsyncSession
from app.repositories.compartment_repository import (
    save_compartment,
    get_compartments,
    delete_compartment,
    update_compartment,
    get_compartment,
)
from app.schemas.compartment import (
    CompartmentCreate,
    CompartmentRequest,
    CompartmentResponse,
)

router = APIRouter()


@router.get(
    "/compartments/furniture/{furn_id}", response_model=list[CompartmentResponse]
)
async def get_compartments_in_furniture(
    furn_id: int, db: AsyncSession = Depends(get_db)
):
    """Get a list of compartments associated with a specific furniture ID.

    This endpoint retrieves all compartments that belong to a specific piece of furniture. 
    It takes the furniture ID as a path parameter and returns a list of `CompartmentResponse` 
    models.

    Args:
        furn_id (int): The ID of the furniture to fetch compartments for.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[CompartmentResponse]: A list of compartments associated with the specified furniture.
    """
    compartments = await get_compartments(db, furn_id)
    return [
        CompartmentResponse.model_validate(compartment, from_attributes=True)
        for compartment in compartments
    ]


@router.get("/compartments/{comp_id}", response_model=CompartmentResponse)
async def get_single_compartment(comp_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve a single compartment by its ID.

    This endpoint fetches a specific compartment from the database by its unique ID and 
    returns it as a `CompartmentResponse`.

    Args:
        comp_id (int): The ID of the compartment to retrieve.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        CompartmentResponse: A compartment object with the specified ID.
    """
    compartment = await get_compartment(db, comp_id)
    return CompartmentResponse.model_validate(compartment, from_attributes=True)


@router.post(
    "/compartments/",
    status_code=status.HTTP_201_CREATED,
    response_model=CompartmentResponse,
)
async def create_compartment(
    compartment: CompartmentCreate, db: AsyncSession = Depends(get_db)
):
    """Create a new compartment.

    This endpoint allows the creation of a new compartment. The details of the compartment 
    are passed in the request body as a `CompartmentCreate` model, and the newly created 
    compartment is returned in the response.

    Args:
        compartment (CompartmentCreate): The details of the new compartment to create.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        CompartmentResponse: The created compartment details.
    """
    created_compartment = await save_compartment(db, compartment)
    return CompartmentResponse.model_validate(created_compartment, from_attributes=True)


@router.delete("/compartments/{comp_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_compartment(comp_id: int, db: AsyncSession = Depends(get_db)):
    """Delete a compartment by its ID.

    This endpoint allows for the deletion of a compartment by its unique ID. 
    It will remove the specified compartment from the database.

    Args:
        comp_id (int): The ID of the compartment to delete.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: A successful response indicating the resource was deleted.
    """
    await delete_compartment(db, comp_id)


@router.put("/compartments/", response_model=CompartmentResponse)
async def update_single_compartment(
    compartment: CompartmentRequest, db: AsyncSession = Depends(get_db)
):
    """Update an existing compartment.

    This endpoint allows updating the details of an existing compartment. The updated 
    compartment details are passed in the request body as a `CompartmentRequest` model, 
    and the updated compartment is returned in the response.

    Args:
        compartment (CompartmentRequest): The updated details of the compartment.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        CompartmentResponse: The updated compartment details.
    """
    updated_compartment = await update_compartment(db, compartment)
    return CompartmentResponse.model_validate(updated_compartment, from_attributes=True)
