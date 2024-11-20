from app.schemas.item import ItemRequest, ItemCreate
from app.models.models import Item


def map_itemCreate_to_item(item_data: ItemCreate) -> Item:
    return Item(
        item_name=item_data.item_name,
        comp_id=item_data.comp_id,
    )
