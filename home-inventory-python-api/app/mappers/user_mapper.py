from app.models.models import User
from app.schemas.user import UserCreate
import datetime


def map_userCreate_to_user(user_create: UserCreate) -> User:
    """Maps a UserCreate schema object to a User model object.

    This function converts the data from the `UserCreate` Pydantic schema, which is used for creating new users,
    into a `User` model object that can be used for database operations, typically during the process of registering
    a new user in the system. The function also ensures that missing fields (like `activated`, `activation_date`, and 
    `activation_code`) are assigned default values if not provided.

    Args:
        user_create (UserCreate): The input schema object containing the details for the user to be created.

    Returns:
        User: The `User` model populated with the data from the `UserCreate` schema, including default values 
              for optional fields.
    """
    return User(
        username=user_create.username,
        email=user_create.email,
        password=user_create.password,
        activated=user_create.activated if user_create.activated is not None else False,
        activation_date=(
            user_create.activation_date
            if user_create.activation_date is not None
            else datetime.datetime.now()
        ),
        activation_code=(
            user_create.activation_code
            if user_create.activation_code is not None
            else "N/A"
        ),
    )
