from app.schemas.item import ItemCreate
from app.models.models import Item


def map_itemCreate_to_item(item_data: ItemCreate) -> Item:
    """Maps an ItemCreate schema object to an Item model object.

    This function converts data from the `ItemCreate` Pydantic schema, which is used for creating new items,
    into an `Item` model object that can be used for database operations, typically during the process of adding 
    a new item to the inventory.

    Args:
        item_data (ItemCreate): The input schema object containing the details for the item to be created.

    Returns:
        Item: The `Item` model populated with the data from the `ItemCreate` schema.
    """
    return Item(
        item_name=item_data.item_name,
        comp_id=item_data.comp_id,
    )
