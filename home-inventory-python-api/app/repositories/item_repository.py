from app.models.models import Item, Tag, Compartment, Furniture, Room
from app.schemas.item import ItemCreate, ItemRequest, ItemMoveRequest
from app.mappers.item_mapper import map_itemCreate_to_item
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload, selectinload
from sqlalchemy import func, select, delete, union
from app.exceptions.custom_exceptions import (
    ItemNotFoundException, 
    CompartmentNotFoundException,
    ItemMovementBetweenHomesNotAllowed,
    ItemUpdatingException,
    UnspecifiedHomeIdException,
)

async def get_items_from_compartment(session: AsyncSession, comp_id: int):
    """
    Retrieves all items in the specified compartment.
    
    Args:
        session (`AsyncSession`): The database session.
        comp_id (`int`): The ID of the compartment.

    Raises:
        `ItemNotFoundException`: If no items are found in the compartment.

    Returns:
        List[`Item`]: A list of items found in the compartment.
    """
    stmt = select(Item).where(Item.comp_id == comp_id)
    result = await session.execute(stmt)
    
    items = result.scalars().all()
    
    if not items:
        raise ItemNotFoundException()
    
    return

async def get_item(session: AsyncSession, item_id: int):
    """
    Retrieves a single item based on its ID.

    Args:
        session (`AsyncSession`): The database session.
        item_id (`int`): The ID of the item.

    Raises:
        `ItemNotFoundException`: If the item is not found.

    Returns:
        `Item`: The item object.
    """
    stmt = select(Item).where(Item.id == item_id)
    result = await session.execute(stmt)
    
    item = result.scalar_one_or_none()
    
    if not item:
        raise ItemNotFoundException()
    
    return item


async def get_route(session: AsyncSession, item_id: int):
    """
    Retrieves the full route of an item through its relationships
    (Home > Room > Furniture > Compartment).

    Args:
        session (`AsyncSession`): The database session.
        item_id (`int`): The ID of the item.

    Raises:
        `ItemNotFoundException`: If the item is not found.

    Returns:
        `str`: The full route of the item.
    """
    stmt = (
        select(Item)
        .options(
            joinedload(Item.compartment)
            .joinedload(Compartment.furniture)
            .joinedload(Furniture.room)
            .joinedload(Room.home)
        )
        .where(Item.id == item_id)
    )
    result = await session.execute(stmt)
    item = result.scalar_one_or_none()

    if not item:
        raise ItemNotFoundException()

    compartment_name = (
        item.compartment.comp_name if item.compartment else "Unknown Compartment"
    )
    furniture_name = (
        item.compartment.furniture.furn_name
        if item.compartment and item.compartment.furniture
        else "Unknown Furniture"
    )
    room_name = (
        item.compartment.furniture.room.room_name
        if item.compartment
        and item.compartment.furniture
        and item.compartment.furniture.room
        else "Unknown Room"
    )
    home_name = (
        item.compartment.furniture.room.home.home_name
        if item.compartment
        and item.compartment.furniture
        and item.compartment.furniture.room
        and item.compartment.furniture.room.home
        else "Unknown Home"
    )

    route = f"{home_name}/{room_name}/{furniture_name}/{compartment_name}"

    return route


async def get_items_by_string(session: AsyncSession, search_string: str, home_id: int):
    """
    Retrieves items by searching for a string in item names or associated tags
    within a specific home.

    Args:
        session (`AsyncSession`): The database session.
        search_string (`str`): The search keyword.
        home_id (`int`): The ID of the home.

    Returns:
        List[`Item`]: A list of items matching the search criteria.
    """
    search_pattern = f"%{search_string.lower()}%"

    items_in_home_by_name = (
        select(Item)
        .join(Item.compartment)
        .join(Compartment.furniture)
        .join(Furniture.room)
        .where(Room.home_id == home_id)
        .where(func.lower(Item.item_name).ilike(search_pattern))
    )

    items_in_home_by_tag = (
        select(Item)
        .join(Item.compartment)
        .join(Compartment.furniture)
        .join(Furniture.room)
        .join(Item.tags)
        .where(Room.home_id == home_id)
        .where(func.lower(Tag.tag_name).ilike(search_pattern))
    )

    combined_query = union(items_in_home_by_name, items_in_home_by_tag)

    result = await session.execute(combined_query.options(selectinload(Item.tags)))

    items = result.fetchall()

    return items


async def update_item(session: AsyncSession, update_data: ItemRequest):
    """
    Updates an item's name and tags.

    Args:
        session (`AsyncSession`): The database session.
        update_data (`ItemRequest`): The update details.

    Raises:
        `ItemNotFoundException`: If the item is not found.

    Returns:
        `Item`: The updated item object.
    """
    try:
        item_id = update_data.id

        stmt = select(Item).where(Item.id == item_id)
        result = await session.execute(stmt)
        item = result.scalar_one_or_none()

        if not item:
            raise ItemNotFoundException()

        if update_data.item_name:
            item.item_name = update_data.item_name

        #if update_data.tags is not None:
        if update_data.tags:
            new_tags = []
            for tag_data in update_data.tags:

                tag_name = tag_data.tag_name
                home_id = tag_data.home_id

                stmt = (
                    select(Tag)
                    .where(Tag.home_id == home_id)
                    .where(func.lower(Tag.tag_name) == func.lower(tag_name))
                )
                result = await session.execute(stmt)
                tag = result.scalar_one_or_none()

                if tag is None:
                    tag = Tag(tag_name=tag_name, home_id=home_id)
                    session.add(tag)

                new_tags.append(tag)

            item.tags = new_tags
        elif update_data.tags == []:
            item.tags = []

        await session.commit()
        await session.refresh(item)
    except Exception as e:
        await session.rollback()
        raise ItemUpdatingException() from e

    return item


async def save_item(session: AsyncSession, item_data: ItemCreate):
    """
    Creates and saves a new item in a specified compartment.

    Args:
        session (`AsyncSession`): The database session.
        item_data (`ItemCreate`): The details of the new item.

    Raises:
        `CompartmentNotFoundException`: If the compartment does not exist.
        `UnspecifiedHomeIdException`: If the associated home cannot be determined.

    Returns:
        `Item`: The created item object.
    """
    stmt = select(Compartment).where(Compartment.id == item_data.comp_id)
    result = await session.execute(stmt)
    compartment = result.scalar_one_or_none()

    if not compartment:
        raise CompartmentNotFoundException()


    home_id = (
        await session.execute(
            select(Room.home_id)
            .join(Furniture, Furniture.room_id == Room.id)
            .join(Compartment, Compartment.furn_id == Furniture.id)
            .where(Compartment.id == item_data.comp_id)
        )
    ).scalar_one_or_none()

    if not home_id:
        raise UnspecifiedHomeIdException()

    new_item = map_itemCreate_to_item(item_data)

    if item_data.tags:
        for tag_name in item_data.tags:
            
            stmt = (
                select(Tag)
                .where(Tag.home_id == home_id)
                .where(func.lower(Tag.tag_name) == func.lower(tag_name))
            )
            result = await session.execute(stmt)
            tag = result.scalar_one_or_none()

            if tag is None:
                tag = Tag(tag_name=tag_name, home_id=home_id)
                session.add(tag)

            new_item.tags.append(tag)

    session.add(new_item)
    await session.commit()
    await session.refresh(new_item)

    return new_item

async def move_item(session: AsyncSession, itemMoveRequest: ItemMoveRequest):
    """
    Moves an item to a different compartment, ensuring both compartments belong to the same home.

    Args:
        session (`AsyncSession`): The database session.
        itemMoveRequest (`ItemMoveRequest`): The details of the move operation.

    Raises:
        `ItemMovementBetweenHomesNotAllowed`: If the compartments are located in different homes.
        `ItemNotFoundException`: If the item does not exist.
    """
    item : Item = await get_item(session, itemMoveRequest.item_id)
    
    change_allowed : bool = are_compartments_in_same_home(session, item.comp_id, itemMoveRequest.comp_id)
    
    if not change_allowed:
        raise ItemMovementBetweenHomesNotAllowed()
    
    if not item:
        raise ItemNotFoundException()
    
    item.comp_id = itemMoveRequest.comp_id
    session.add(item)
    await session.commit()

async def are_compartments_in_same_home(session: AsyncSession, source_comp_id: int, destination_comp_id) -> bool:
    """
    Checks whether two compartments belong to the same home.

    Args:
        session (`AsyncSession`): The database session.
        source_comp_id (`int`): The ID of the source compartment.
        destination_comp_id (`int`): The ID of the destination compartment.

    Returns:
        `bool`: True if the compartments are in the same home, False otherwise.
    """
    source_home_id = (
        await session.execute(
            select(Room.home_id)
            .join(Furniture, Furniture.room_id == Room.id)
            .join(Compartment, Compartment.furn_id == Furniture.id)
            .where(Compartment.id == source_comp_id)
        )
    ).scalar_one_or_none()
    
    destination_home_id = (
        await session.execute(
            select(Room.home_id)
            .join(Furniture, Furniture.room_id == Room.id)
            .join(Compartment, Compartment.furn_id == Furniture.id)
            .where(Compartment.id == destination_comp_id)
        )
    ).scalar_one_or_none()
    
    return source_home_id != destination_home_id


async def delete_item(session: AsyncSession, item_id: int):
    """
    Deletes an item and clears its associated tags.

    Args:
        session (`AsyncSession`): The database session.
        item_id (`int`): The ID of the item to delete.

    Raises:
        `ItemNotFoundException`: If the item does not exist.
    """
    stmt = select(Item).where(Item.id == item_id)
    result = await session.execute(stmt)
    item = result.scalar_one_or_none()

    if not item:
        raise ItemNotFoundException()

    item.tags = []

    await session.refresh(item)

    await session.execute(delete(Item).where(Item.id == item_id))
    await session.commit()
