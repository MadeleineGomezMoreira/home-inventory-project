from pydantic import BaseModel, ConfigDict
from typing import List
from app.utils.camel_case_generator import camel_case_generator


class HomeBase(BaseModel):
    """Base model for Home data.

    This model serves as the base class for the Home-related models and contains 
    common attributes. It is meant to be inherited by other models related to homes.

    Attributes:
        home_name (`str`): The name of the home.
        owned_by (`int`): The ID of the user who owns the home.

    Model Configuration:
        - from_attributes (`bool`): Enables reading attributes from the model for serialization.
    """
    home_name: str
    owned_by: int

    model_config = ConfigDict(from_attributes=True)


class HomeCreate(HomeBase):
    """Model used for creating a new home.

    This model inherits from HomeBase and specifies additional configuration
    for creating a new home. It includes alias generation for field names
    to follow camelCase style for JSON serialization.

    Attributes:
        home_name (`str`): The name of the home.
        owned_by (`int`): The ID of the user who owns the home.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    home_name: str
    owned_by: int

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class HomeResponse(HomeBase):
    """Response model for home data.

    This model is used for returning home data in API responses. It inherits from
    HomeBase and includes an `id` field that uniquely identifies the home.

    Attributes:
        id (`int`): The unique ID of the home.
        home_name (`str`): The name of the home.
        owned_by (`int`): The ID of the user who owns the home.

    Model Configuration:
        - from_attributes (`bool`): Enables reading attributes from the model for serialization.
        - extra (`str`): Ignores any extra fields not defined in the model.
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    home_name: str
    owned_by: int

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class HomesByRoleResponse(BaseModel):
    """Response model for listing homes by role.

    This model groups homes by roles (OWNER and MEMBER), returning a list of homes
    associated with each role. It is used to represent the relationship between
    users and homes based on their roles.

    Attributes:
        OWNER (List[`HomeResponse`]): A list of homes where the user is an owner.
        MEMBER (List[`HomeResponse`]): A list of homes where the user is a member.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    OWNER: List[HomeResponse]
    MEMBER: List[HomeResponse]

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class HomeRequest(BaseModel):
    """Request model for home data.

    This model is used for updating or modifying existing home data. It contains
    only the necessary fields for handling the request and excludes fields like `owned_by`.

    Attributes:
        id (`int`): The unique ID of the home.
        home_name (`str`): The name of the home.

    Model Configuration:
        - from_attributes (`bool`): Enables reading attributes from the model for serialization.
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    home_name: str
    model_config = ConfigDict(from_attributes=True)

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
