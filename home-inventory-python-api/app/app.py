from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from app.exceptions.custom_exceptions import (
    DatabaseException,
    HomeUpdatingException,
    SavingRoomException, 
    SavingHomeException,
    SavingFurnitureException,
    SavingCompartmentException,
    SavingItemException,
    HomeNotFoundException,
    UserNotFoundException,
    RoomNotFoundException,
    FurnitureNotFoundException,
    CompartmentNotFoundException,
    ItemNotFoundException,
    InvitationNotFoundException,
    UserNotActivatedException,
    AccountAlreadyActivatedException,
    MailMessagingException,
    WrongUserCredentialsException,
    ActivationCodeExpiredException,
    InvalidActivationCodeException,
    UserAlreadyInHomeException,
    UserNotInHomeException,
    InvitationAlreadyExistsException,
    ItemUpdatingException,
    CompartmentUpdatingException,
    UnspecifiedHomeIdException,
    FurnitureUpdatingException,
)
from app.routes import api_router

app = FastAPI()

app.include_router(api_router)

# Exception handling

@app.exception_handler(DatabaseException)
async def database_exception_handler(request: Request, exception: DatabaseException):
    """Handle raised generic database-related exceptions.

    Args:
        request (Request): The incoming request.
        exception (DatabaseException): The raised exception.

    Returns:
        JSONResponse: A 500 Internal Server Error response.
    """
    return JSONResponse(
        status_code=500,
        content={"detail": str(exception)},
    )

@app.exception_handler(SavingHomeException)
async def saving_home_exception_handler(request: Request, exception: SavingHomeException):
    """Handle exceptions raised when saving a home fails.

    Args:
        request (Request): The incoming request.
        exception (SavingHomeException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(SavingRoomException)
async def saving_room_exception_handler(request: Request, exception: SavingRoomException):
    """Handle exceptions raised when saving a room fails.

    Args:
        request (Request): The incoming request.
        exception (SavingRoomException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(SavingFurnitureException)
async def saving_furniture_exception_handler(request: Request, exception: SavingFurnitureException):
    """Handle exceptions raised when saving furniture fails.

    Args:
        request (Request): The incoming request.
        exception (SavingFurnitureException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(SavingCompartmentException)
async def saving_compartment_exception_handler(request: Request, exception: SavingCompartmentException):
    """Handle exceptions raised when saving a compartment fails.

    Args:
        request (Request): The incoming request.
        exception (SavingCompartmentException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(SavingItemException)
async def saving_item_exception_handler(request: Request, exception: SavingItemException):
    """Handle exceptions raised when saving an item fails.

    Args:
        request (Request): The incoming request.
        exception (SavingItemException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(HomeNotFoundException)
async def home_not_found_exception_handler(request: Request, exception: HomeNotFoundException):
    """Handle exceptions raised when a home is not found.

    Args:
        request (Request): The incoming request.
        exception (HomeNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(UserNotFoundException)
async def user_not_found_exception_handler(request: Request, exception: UserNotFoundException):
    """Handle exceptions raised when a user is not found.

    Args:
        request (Request): The incoming request.
        exception (UserNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(RoomNotFoundException)
async def room_not_found_exception_handler(request: Request, exception: RoomNotFoundException):
    """Handle exceptions raised when a room is not found.

    Args:
        request (Request): The incoming request.
        exception (RoomNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(FurnitureNotFoundException)
async def furniture_not_found_exception_handler(request: Request, exception: FurnitureNotFoundException):
    """Handle exceptions raised when a piece of furniture is not found.

    Args:
        request (Request): The incoming request.
        exception (FurnitureNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(CompartmentNotFoundException)
async def compartment_not_found_exception_handler(request: Request, exception: CompartmentNotFoundException):
    """Handle exceptions raised when a compartment is not found.

    Args:
        request (Request): The incoming request.
        exception (CompartmentNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(ItemNotFoundException)
async def item_not_found_exception_handler(request: Request, exception: ItemNotFoundException):
    """Handle exceptions raised when an item is not found.

    Args:
        request (Request): The incoming request.
        exception (ItemNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(InvitationNotFoundException)
async def invitation_not_found_exception_handler(request: Request, exception: InvitationNotFoundException):
    """Handle exceptions raised when an invitation is not found.

    Args:
        request (Request): The incoming request.
        exception (InvitationNotFoundException): The raised exception.

    Returns:
        JSONResponse: A 404 Not Found response.
    """
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )

@app.exception_handler(UserNotActivatedException)
async def user_not_activated_exception_handler(request: Request, exception: UserNotActivatedException):
    """Handle exceptions when a user account is not activated.

    Args:
        request (Request): The incoming request.
        exception (UserNotActivatedException): The raised exception.

    Returns:
        JSONResponse: A 403 Forbidden response.
    """
    return JSONResponse(
        status_code=403,
        content={"detail": str(exception)},
    )

@app.exception_handler(AccountAlreadyActivatedException)
async def account_already_activated_exception_handler(request: Request, exception: AccountAlreadyActivatedException):
    """Handle exceptions when an activated user account is trying to be activated.

    Args:
        request (Request): The incoming request.
        exception (AccountAlreadyActivatedException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )
    
@app.exception_handler(MailMessagingException)
async def mail_messaging_exception_handler(request: Request, exception: MailMessagingException):
    """Handle exceptions related to mail messaging failures.

    Args:
        request (Request): The incoming request.
        exception (MailMessagingException): The raised exception.

    Returns:
        JSONResponse: A 500 Internal Server Error response.
    """
    return JSONResponse(
        status_code=500,
        content={"detail": str(exception)},
    )

@app.exception_handler(WrongUserCredentialsException)
async def wrong_user_credentials_exception_handler(request: Request, exception: WrongUserCredentialsException):
    """Handle exceptions when user credentials are incorrect.

    Args:
        request (Request): The incoming request.
        exception (WrongUserCredentialsException): The raised exception.

    Returns:
        JSONResponse: A 401 Unauthorized response.
    """
    return JSONResponse(
        status_code=401,
        content={"detail": str(exception)},
    )

@app.exception_handler(ActivationCodeExpiredException)
async def activation_code_expired_exception_handler(request: Request, exception: ActivationCodeExpiredException):
    """Handle exceptions when an activation code has expired.

    Args:
        request (Request): The incoming request.
        exception (ActivationCodeExpiredException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )
    
@app.exception_handler(InvalidActivationCodeException)
async def invalid_activation_code_exception_handler(request: Request, exception: InvalidActivationCodeException):
    """Handle exceptions when an activation code is invalid.

    Args:
        request (Request): The incoming request.
        exception (InvalidActivationCodeException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )
    
@app.exception_handler(UserAlreadyInHomeException)
async def user_already_in_home_exception_handler(request: Request, exception: UserAlreadyInHomeException):
    """Handle exceptions when a user is already part of a home.

    Args:
        request (Request): The incoming request.
        exception (UserAlreadyInHomeException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(UserNotInHomeException)
async def user_not_in_home_exception_handler(request: Request, exception: UserNotInHomeException):
    """Handle exceptions when a user is not part of a home.

    Args:
        request (Request): The incoming request.
        exception (UserNotInHomeException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(InvitationAlreadyExistsException)
async def invitation_already_exists_exception_handler(request: Request, exception: InvitationAlreadyExistsException):
    """Handle exceptions when an invitation already exists.

    Args:
        request (Request): The incoming request.
        exception (InvitationAlreadyExistsException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(ItemUpdatingException)
async def item_updating_exception_handler(request: Request, exception: ItemUpdatingException):
    """Handle exceptions raised when updating an item fails.

    Args:
        request (Request): The incoming request.
        exception (ItemUpdatingException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )
    
@app.exception_handler(CompartmentUpdatingException)
async def compartment_updating_exception_handler(request: Request, exception: CompartmentUpdatingException):
    """Handle exceptions raised when updating a compartment fails.

    Args:
        request (Request): The incoming request.
        exception (CompartmentUpdatingException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )
    
@app.exception_handler(FurnitureUpdatingException)
async def furniture_updating_exception_handler(request: Request, exception: FurnitureUpdatingException):
    """Handle exceptions raised when updating furniture fails.

    Args:
        request (Request): The incoming request.
        exception (FurnitureUpdatingException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(HomeUpdatingException)
async def home_updating_exception_handler(request: Request, exception: HomeUpdatingException):
    """Handle exceptions raised when updating a home fails.

    Args:
        request (Request): The incoming request.
        exception (HomeUpdatingException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )

@app.exception_handler(UnspecifiedHomeIdException)
async def unspecified_home_id_exception_handler(request: Request, exception: UnspecifiedHomeIdException):
    """Handle exceptions when a home ID is not specified.

    Args:
        request (Request): The incoming request.
        exception (UnspecifiedHomeIdException): The raised exception.

    Returns:
        JSONResponse: A 400 Bad Request response.
    """
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )




