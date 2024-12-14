from typing import List, Optional
from pydantic import BaseModel, ConfigDict
from app.utils.camel_case_generator import camel_case_generator


class ItemBase(BaseModel):
    """Base model for item data.

    This model serves as the foundation for item-related models and contains
    the basic attributes for an item. It is meant to be inherited by other 
    item-related models.

    Attributes:
        item_name (`str`): The name of the item.
        comp_id (`int`): The ID of the compartment to which the item belongs.
    """
    item_name: str
    comp_id: int


class TagResponse(BaseModel):
    """Response model for tag data.

    This model is used to return detailed information about tags associated 
    with items. It includes the `id`, `tag_name`, and `home_id` for the tag.

    Attributes:
        id (`int`): The unique identifier for the tag.
        tag_name (`str`): The name of the tag.
        home_id (`int`): The ID of the home associated with the tag.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    tag_name: str
    home_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class ItemCreate(ItemBase):
    """Model used for creating a new item.

    This model extends `ItemBase` and includes additional fields related to 
    the creation of an item. The `tags` field is optional and can be used 
    to associate tags with the item.

    Attributes:
        tags (Optional[List[`str`]]): A list of tags associated with the item.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    tags: Optional[List[str]] = None

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class ItemRequest(ItemBase):
    """Request model for updating or modifying an item.

    This model extends `ItemBase` and includes the `id` of the item along 
    with a list of associated tags for the item. The `tags` field is optional 
    and contains `TagResponse` objects.

    Attributes:
        id (`int`): The unique identifier for the item.
        tags (Optional[List[`TagResponse`]]): A list of tag objects associated with the item.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    tags: Optional[List[TagResponse]] = None

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
    
class ItemMoveRequest(BaseModel):
    """Request model for moving an item between compartments.

    This model is used when an item is moved to a different compartment. 
    It includes the `item_id` (ID of the item to be moved) and the `comp_id` 
    (ID of the target compartment).

    Attributes:
        item_id (`int`): The ID of the item to be moved.
        comp_id (`int`): The ID of the target compartment where the item will be moved.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    item_id: int
    comp_id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class ItemResponse(ItemBase):
    """Response model for item data with tags.

    This model is used to return detailed information about an item in an 
    API response, including the `id`, `tags`, and other item attributes.

    Attributes:
        id (`int`): The unique identifier for the item.
        tags (List[`TagResponse`]): A list of `TagResponse` objects associated with the item.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int
    tags: List[TagResponse] = []

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )


class ItemResponseNoTags(ItemBase):
    """Response model for item data without tags.

    This model is used to return basic information about an item without 
    the associated tags. It includes the `id` and other basic item attributes.

    Attributes:
        id (`int`): The unique identifier for the item.

    Model Configuration:
        - from_attributes (`bool`): Ensures that the field names match during deserialization.
        - populate_by_name (`bool`): Ensures that the field names match between JSON and Python.
        - alias_generator (`function`): Generates camelCase aliases for the fields when serialized.
    """
    id: int

    model_config = ConfigDict(
        from_attributes=True,
        populate_by_name=True,
        alias_generator=camel_case_generator,
    )
