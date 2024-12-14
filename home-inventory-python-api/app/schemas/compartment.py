from pydantic import BaseModel, ConfigDict
from app.utils.camel_case_generator import camel_case_generator


class CompartmentBase(BaseModel):
    """Base model for the compartment entity.

    This model contains the common fields for all compartment operations.
    It is inherited by the other compartment models to enforce consistency in the fields.

    Attributes:
        comp_name (`str`): The name of the compartment.
        furn_id (`int`): The ID of the furniture associated with the compartment.

    Model Configuration:
        - from_attributes (`bool`): Ensures that attributes are used for serialization and deserialization.
    """
    comp_name: str
    furn_id: int

    model_config = ConfigDict(from_attributes=True)


class CompartmentCreate(CompartmentBase):
    """Model used for creating a new compartment.

    This model inherits from CompartmentBase and specifies additional configuration
    for creating a new compartment. It includes alias generation for field names
    to follow camelCase style for JSON serialization.

    Attributes:
        comp_name (`str`): The name of the compartment.
        furn_id (`int`): The ID of the furniture associated with the compartment.

    Model Configuration:
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )



class CompartmentResponse(CompartmentBase):
    """Response model for retrieving a compartment.

    This model extends CompartmentBase and is used when retrieving a compartment's
    details. It includes the compartment ID and additional configuration for handling
    field aliasing during serialization.

    Attributes:
        id (`int`): The unique identifier of the compartment.
        comp_name (`str`): The name of the compartment.
        furn_id (`int`): The ID of the furniture associated with the compartment.

    Model Configuration:
        - from_attributes (`bool`): Ensures that attributes are used for serialization and deserialization.
        - extra (str): Specifies how to handle extra fields (set to 'ignore').
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    comp_name: str
    furn_id: int

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class CompartmentRequest(BaseModel):
    """Request model for updating an existing compartment.

    This model is used when updating a compartment's details. It contains
    the compartment ID and name for the update operation. It also includes
    configuration to map fields to camelCase during serialization.

    Attributes:
        id (`int`): The unique identifier of the compartment to update.
        comp_name (`str`): The new name of the compartment.

    Model Configuration:
        - from_attributes (`bool`): Ensures that attributes are used for serialization and deserialization.
        - extra (`str`): Specifies how to handle extra fields (set to 'ignore').
        - populate_by_name (`bool`): Ensures that the field names match during deserialization.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    comp_name: str

    model_config = ConfigDict(
        from_attributes=True,
        extra="ignore",
        populate_by_name=True,
        alias_generator=camel_case_generator
    )
