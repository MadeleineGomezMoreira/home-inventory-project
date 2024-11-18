from app.models.models import Item, Tag, Compartment, Furniture, Room
from app.schemas.item import ItemCreate, ItemRequest
from app.mappers.item_mapper import map_itemCreate_to_item
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload, selectinload
from sqlalchemy import func, select, delete, union
from app.exceptions.custom_exceptions import ItemNotFoundError, CompartmentNotFoundError


# Retrieve all items from a compartment
async def get_items_from_compartment(session: AsyncSession, comp_id: int):
    stmt = (
        select(Item)
        .options(selectinload(Item.tags))  # Eager load the tags
        .where(Item.comp_id == comp_id)
    )
    result = await session.execute(stmt)
    return result.scalars().all()


# Retrieve item by name and tag
async def get_items_by_string(session: AsyncSession, search_string: str, home_id: int):
    # Query items by name
    items_in_home_by_name = (
        select(Item)
        .join(Item.compartment)
        .join(Compartment.furniture)
        .join(Furniture.room)
        .where(Room.home_id == home_id)
        .where(func.lower(Item.item_name) == func.lower(search_string))
    )

    # Query items by tag
    items_in_home_by_tag = (
        select(Item)
        .join(Item.compartment)
        .join(Compartment.furniture)
        .join(Furniture.room)
        .join(Item.tags)
        .where(Room.home_id == home_id)
        .where(func.lower(Tag.tag_name) == func.lower(search_string))
    )

    # Combine both queries using UNION
    combined_query = union(items_in_home_by_name, items_in_home_by_tag)

    # Execute the combined query
    result = await session.execute(combined_query.options(selectinload(Item.tags)))

    # Retrieve and return items
    return result.scalars().all()


# Save new item to a compartment
async def save_item(session: AsyncSession, item_data: ItemCreate):
    # First, fetch the compartment based on comp_id from item_data
    stmt = select(Compartment).where(Compartment.id == item_data.comp_id)
    result = await session.execute(stmt)
    compartment = result.scalar_one_or_none()

    if not compartment:
        raise CompartmentNotFoundError(
            f"Compartment with id {item_data.comp_id} not found."
        )

    # Retrieve the associated home_id from the Room model through Compartment -> Furniture -> Room
    home_id = (
        await session.execute(
            select(Room.home_id)
            .join(Furniture, Furniture.room_id == Room.id)
            .join(Compartment, Compartment.furn_id == Furniture.id)
            .where(Compartment.id == item_data.comp_id)
        )
    ).scalar_one_or_none()

    if not home_id:
        raise ValueError(
            "Home ID could not be determined for the specified compartment."
        )

    # Create and add the new Item
    new_item = map_itemCreate_to_item(item_data)

    # Handle tags if provided
    if item_data.tags:
        for tag_name in item_data.tags:

            # Check if tag exists or create a new one
            stmt = (
                select(Tag)
                .where(Tag.home_id == home_id)
                .where(func.lower(Tag.tag_name) == func.lower(tag_name))
            )
            result = await session.execute(stmt)
            tag = result.scalar_one_or_none()

            # If tag doesn't exist, create it
            if tag is None:
                tag = Tag(tag_name=tag_name, home_id=home_id)
                session.add(tag)

            # Link the tag to the new item (many-to-many relationship)
            new_item.tags.append(tag)

    session.add(new_item)
    # Commit the changes
    await session.commit()
    await session.refresh(new_item)

    return new_item
