from fastapi import APIRouter, Depends, status
from app.database.db_engine import get_db
from sqlalchemy.ext.asyncio import AsyncSession
from app.repositories.compartment_repository import (
    save_compartment,
    get_compartments,
    delete_compartment,
    update_compartment,
)
from app.schemas.compartment import (
    CompartmentCreate,
    CompartmentRequest,
    CompartmentResponse,
)

router = APIRouter()


@router.get("/compartments/{furn_id}", response_model=list[CompartmentResponse])
async def get_compartments_in_furniture(
    furn_id: int, db: AsyncSession = Depends(get_db)
):
    compartments = await get_compartments(db, furn_id)
    return [
        CompartmentResponse.model_validate(compartment, from_attributes=True)
        for compartment in compartments
    ]


@router.post(
    "/compartments/",
    status_code=status.HTTP_201_CREATED,
    response_model=CompartmentResponse,
)
async def create_compartment(
    compartment: CompartmentCreate, db: AsyncSession = Depends(get_db)
):
    created_compartment = await save_compartment(db, compartment)
    return CompartmentResponse.model_validate(created_compartment, from_attributes=True)


@router.delete("/compartments/{comp_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_compartment(comp_id: int, db: AsyncSession = Depends(get_db)):
    await delete_compartment(db, comp_id)


@router.put("/compartments/", response_model=CompartmentResponse)
async def update_single_compartment(
    compartment: CompartmentRequest, db: AsyncSession = Depends(get_db)
):
    updated_compartment = await update_compartment(db, compartment)
    return CompartmentResponse.model_validate(updated_compartment, from_attributes=True)
