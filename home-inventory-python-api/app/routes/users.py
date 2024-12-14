from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from app.schemas.user import UserResponse, UserCreate, UsersByRoleResponse, UserRequestLogin
from app.database.db_engine import get_db
from app.repositories.user_repository import (
    get_all_users,
    get_all_users_by_home_by_role,
    get_user_by_id,
    get_user_by_username,
    save_user,
    login_by_user_password,
    send_email,
    activate_user_account,
    delete_user
)
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()

@router.delete("/users/{user_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_single_user(user_id: int, db: AsyncSession = Depends(get_db)):
    """Delete a user by their ID.

    This endpoint allows the deletion of a user from the database by providing their `user_id`.
    The user is permanently removed from the system.

    Args:
        user_id (int): The ID of the user to delete.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        status.HTTP_204_NO_CONTENT: A successful response indicating the user was deleted.
    """
    await delete_user(db, user_id)

@router.get(
    "/users/", 
)
async def read_all_users(db: AsyncSession = Depends(get_db)):
    """Retrieve a list of all users.

    This endpoint retrieves all users in the system and returns their details in a list of `UserResponse` objects.

    Args:
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        list[UserResponse]: A list of all users in the system.
    """
    users = await get_all_users(db)
    return [UserResponse.model_validate(user, from_attributes=True) for user in users]


@router.get(
    "/users/{user_id}", response_model=UserResponse
)
async def read_user_by_id(user_id: int, db: AsyncSession = Depends(get_db)):
    """Retrieve a user by their ID.

    This endpoint retrieves a specific user from the database by their unique `user_id`. If the user
    is not found, it raises a `404 Not Found` exception.

    Args:
        user_id (int): The ID of the user to retrieve.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        UserResponse: The user details in the `UserResponse` format.
    
    Raises:
        HTTPException: If the user is not found by the provided `user_id`, a `404` exception is raised.
    """
    user = await get_user_by_id(db, user_id)
    if user is None:
        raise HTTPException(status_code=404, detail="User not found by id")
    return UserResponse.model_validate(user, from_attributes=True)


@router.get(
    "/users/name/{username}", response_model=UserResponse
)
async def read_user_by_username(username: str, db: Session = Depends(get_db)):
    """Retrieve a user by their username.

    This endpoint retrieves a specific user by their `username`. If the user is not found, it raises 
    a `404 Not Found` exception.

    Args:
        username (str): The username of the user to retrieve.
        db (Session): The database session dependency injected by FastAPI.

    Returns:
        UserResponse: The user details in the `UserResponse` format.

    Raises:
        HTTPException: If the user is not found by the provided `username`, a `404` exception is raised.
    """
    user = await get_user_by_username(db, username)
    if user is None:
        raise HTTPException(status_code=404, detail="User not found by username")
    return UserResponse.model_validate(user, from_attributes=True)


@router.post(
    "/users/", status_code=status.HTTP_201_CREATED, response_model=UserResponse
)
async def create_user(user: UserCreate, db: Session = Depends(get_db)):
    """Create a new user.

    This endpoint creates a new user in the system by accepting a `UserCreate` object with the necessary
    user data. Upon successful creation, the newly created user is returned in the `UserResponse` format.

    Args:
        user (UserCreate): The user data required to create the user.
        db (Session): The database session dependency injected by FastAPI.

    Returns:
        UserResponse: A response containing the newly created user details.
    """
    created_user = await save_user(db, user)
    return UserResponse.model_validate(created_user, from_attributes=True)


@router.get("/users/home/{home_id}", response_model=UsersByRoleResponse)
async def read_all_users_by_home_by_role(
    home_id: int, db: AsyncSession = Depends(get_db)
):
    """Retrieve all users in a home, categorized by their roles.

    This endpoint retrieves all users associated with a specific `home_id`, and organizes them 
    by their roles within the home. The response will categorize users into different roles.

    Args:
        home_id (int): The ID of the home whose users are being fetched.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        UsersByRoleResponse: A response containing users grouped by their roles within the home.
    """
    users = await get_all_users_by_home_by_role(db, home_id)
    return users

@router.post("/login/")
async def login(login_data: UserRequestLogin, db: AsyncSession = Depends(get_db)):
    """Authenticate a user by username and password.

    This endpoint performs user authentication based on the provided `username` and `password`.
    If the credentials are valid, the user is logged in and their `user_id` is returned.

    Args:
        login_data (UserRequestLogin): The login credentials (username and password).
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        user_id (int): The ID of the authenticated user.
    """
    user_id = await login_by_user_password(db, login_data)
    return user_id

@router.put("/resend-code/")
async def resend_activation_code(email: str, db: AsyncSession = Depends(get_db)):
    """Resend the account activation code to the user.

    This endpoint resends the activation code to the user's email address to help them activate their account.

    Args:
        email (str): The email address of the user to resend the activation code.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        None: No response body is returned. A successful resend of the activation code is assumed.
    """
    await send_email(db, email)

@router.get("/activate")
async def activate(email: str, code: str, db: AsyncSession = Depends(get_db)):
    """Activate a user's account using the activation code.

    This endpoint allows the user to activate their account by providing their email and the corresponding 
    activation code. Upon successful activation, the user's account is marked as activated.

    Args:
        email (str): The email address of the user to activate.
        code (str): The activation code sent to the user via email.
        db (AsyncSession): The database session dependency injected by FastAPI.

    Returns:
        None: A successful activation of the account is assumed.
    """
    await activate_user_account(db, email, code)
