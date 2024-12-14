from fastapi import APIRouter, Depends, status
from app.schemas.home import HomeResponse, HomeCreate, HomesByRoleResponse, HomeRequest
from app.database.db_engine import get_db
from app.repositories.home_repository import (
    get_all_homes,
    get_all_homes_by_user_by_role,
    get_home_by_id,
    save_home,
    update_home,
    delete_home,
    remove_user_from_home,
    get_is_user_owner,
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()

@router.get("/homes/{home_id}/ownership/{user_id}")
async def get_ownership(home_id: int, user_id: int, db: AsyncSession = Depends(get_db)):
    """Check if a user is the owner of a specific home.

    This endpoint checks whether a specific user is the owner of the given home.
    
    Args:
        home_id (int): The ID of the home to check ownership for.
        user_id (int): The ID of the user whose ownership status is being checked.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        bool: True if the user is the owner of the home, otherwise False.
    """
    ownership = await get_is_user_owner(db, user_id, home_id)
    return ownership

@router.get("/homes/", response_model=list[HomeResponse])
async def read_all_homes(db: AsyncSession = Depends(get_db)):
    """Retrieve all homes in the database.

    This endpoint retrieves a list of all homes available in the database.
    
    Args:
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[HomeResponse]: A list of all homes in the database.
    """
    homes = await get_all_homes(db)
    return [HomeResponse.model_validate(home, from_attributes=True) for home in homes]


@router.get("/homes/single/{home_id}", response_model=HomeResponse)
async def get_home(home_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve a specific home by its ID.

    This endpoint retrieves the details of a single home based on the home ID.
    
    Args:
        home_id (int): The ID of the home to retrieve.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        HomeResponse: The details of the home with the specified ID.
    """
    home = await get_home_by_id(db, home_id)
    return HomeResponse.model_validate(home, from_attributes=True)


@router.post(
    "/homes/", status_code=status.HTTP_201_CREATED, response_model=HomeResponse
)
async def create_home(home: HomeCreate, db: AsyncSession = Depends(get_db)):
    """Create a new home.

    This endpoint allows the creation of a new home by providing the necessary details 
    in the request body as a `HomeCreate` model. The newly created home is returned 
    in the response.

    Args:
        home (HomeCreate): The details of the new home to create.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        HomeResponse: The details of the newly created home.
    """
    created_home = await save_home(db, home)
    return HomeResponse.model_validate(created_home, from_attributes=True)


@router.get("/homes/{user_id}", response_model=HomesByRoleResponse)
async def read_all_homes_by_user_by_role(
    user_id: int, db: AsyncSession = Depends(get_db)
):
    """Retrieve all homes associated with a user, categorized by their role.

    This endpoint retrieves all homes where a specific user is associated, categorized 
    by their role in the home (e.g., OWNER, MEMBER).
    
    Args:
        user_id (int): The ID of the user to retrieve homes for.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        HomesByRoleResponse: A response containing homes categorized by the user's role.
    """
    homes = await get_all_homes_by_user_by_role(db, user_id)
    return homes


@router.put("/homes/", response_model=HomeResponse)
async def update_single_home(home: HomeRequest, db: AsyncSession = Depends(get_db)):
    """Update an existing home.

    This endpoint allows updating an existing home with new details. The updated home 
    details are passed in the request body as a `HomeRequest` model, and the updated 
    home is returned in the response.

    Args:
        home (HomeRequest): The updated details of the home.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        HomeResponse: The details of the updated home.
    """
    updated_home = await update_home(db, home)
    return HomeResponse.model_validate(updated_home, from_attributes=True)


@router.delete("/homes/{home_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_home(home_id: int, db: AsyncSession = Depends(get_db)):
    """Delete a specific home by its ID.

    This endpoint deletes a home from the database using its home ID. The operation 
    completes with a `204 No Content` status if the home is successfully deleted.

    Args:
        home_id (int): The ID of the home to delete.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: Indicates successful deletion of the home.
    """
    await delete_home(db, home_id)


@router.delete(
    "/homes/{home_id}/kick/{user_id}/", status_code=status.HTTP_204_NO_CONTENT
)
async def kick_user(home_id: int, user_id: int, db: AsyncSession = Depends(get_db)):
    """Remove a user from a home.

    This endpoint removes a specific user from a home, effectively 'kicking' them out.
    It deletes the user from the association between the user and home in the database.
    
    Args:
        home_id (int): The ID of the home to remove the user from.
        user_id (int): The ID of the user to remove from the home.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: Indicates successful removal of the user from the home.
    """
    await remove_user_from_home(db, home_id, user_id)
