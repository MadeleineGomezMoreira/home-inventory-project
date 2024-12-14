from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import List, Optional
from app.utils.camel_case_generator import camel_case_generator


class UserBase(BaseModel):
    """Base model for user data.

    This model is used as the base for creating, updating, and managing user data. 
    It includes essential user information such as `username`, `password`, `email`, 
    and optional fields like `activated`, `activation_date`, and `activation_code`.

    Attributes:
        username (`str`): The unique username for the user.
        password (`str`): The user's password (should be hashed before storage).
        email (`str`): The user's email address.
        activated (Optional[`bool`]): A flag indicating whether the user is activated. Defaults to `False`.
        activation_date (Optional[`datetime`]): The date when the user activated their account.
        activation_code (Optional[`str`]): A code used for account activation.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
    """
    username: str
    password: str
    email: str
    activated: Optional[bool] = False
    activation_date: Optional[datetime] = None
    activation_code: Optional[str] = None

    model_config = ConfigDict(from_attributes=True)


class UserCreate(UserBase):
    """Model for creating a new user.

    This model inherits from `UserBase` and is used for creating a new user account. 
    It requires `username`, `password`, and `email` for registration.

    Attributes:
        username (`str`): The unique username for the user.
        password (`str`): The user's password (should be hashed before storage).
        email (`str`): The user's email address.
    """
    username: str 
    password: str 
    email: str


class UserResponse(BaseModel):
    """Response model for returning user details.

    This model is used to return basic user information, such as the user's `id`, 
    `username`, and `email`. This model is typically used in API responses to 
    provide user data.

    Attributes:
        id (`int`): The unique identifier of the user.
        username (`str`): The user's username.
        email (`str`): The user's email address.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - extra (`str`): Specifies how to handle extra fields not defined in the model. In this case, `extra="ignore"` means extra fields will be ignored.
    """
    id: int
    username: str
    email: str

    model_config = ConfigDict(from_attributes=True, extra="ignore")

class UserRequestLogin(BaseModel):
    """Model for user login request.

    This model is used to capture the user's credentials (username and password) 
    during the login process.

    Attributes:
        username (`str`): The username of the user attempting to log in.
        password (`str`): The password of the user attempting to log in.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
    """
    username: str
    password: str

    model_config = ConfigDict(from_attributes=True)

class UsersByRoleResponse(BaseModel):
    """Response model for grouping users by role.

    This model is used to group users by their roles, with separate lists for users 
    with the role `OWNER` and those with the role `MEMBER`.

    Attributes:
        OWNER (`UserResponse`): A single user with the `OWNER` role.
        MEMBER (List[`UserResponse`]): A list of users with the `MEMBER` role.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match between the JSON and Python representations.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    OWNER: UserResponse
    MEMBER: List[UserResponse]

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
