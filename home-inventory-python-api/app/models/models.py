from sqlalchemy import (
    ForeignKey,
    String,
    Boolean,
    DateTime,
    Enum,
    Table,
    Integer,
    Column,
)
from sqlalchemy.orm import Mapped, mapped_column, relationship
import datetime
from enum import Enum as PyEnum
from app.models.base import Base


class User(Base):
    """
    User model that represents an individual user in the system.

    Attributes:
        id (int): Unique identifier for the user.
        username (str): Unique username for the user (up to 30 characters).
        email (str): Email address of the user (up to 100 characters).
        password (str): Hashed password of the user (up to 100 characters).
        activated (bool): Indicates whether the user has activated their account.
        activation_date (datetime): The date and time when the user activated their account.
        activation_code (str): Activation code for the user, default is 'N/A'.
        homes (list[Home]): A list of homes owned by the user (one-to-many relationship with Home).
        user_homes (list[UserHome]): A list of homes the user belongs to, with roles (many-to-many relationship with UserHome).
        invitations_sent (list[Invitation]): A list of invitations sent by the user (one-to-many relationship with Invitation).
        invitations_received (list[Invitation]): A list of invitations received by the user (one-to-many relationship with Invitation).
    """
    __tablename__ = "user"

    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)
    username: Mapped[str] = mapped_column(String(30), unique=True)
    email: Mapped[str] = mapped_column(String(100))
    password: Mapped[str] = mapped_column(String(100))
    activated: Mapped[bool] = mapped_column(Boolean, default=False)
    activation_date: Mapped[datetime.datetime] = mapped_column(
        DateTime, default=datetime.datetime.now
    )
    activation_code: Mapped[str] = mapped_column(String(512), default="N/A")
    homes: Mapped[list["Home"]] = relationship(
        back_populates="owner", cascade="delete, delete-orphan"
    )
    user_homes: Mapped[list["UserHome"]] = relationship(
        "UserHome", back_populates="user", cascade="delete, delete-orphan"
    )
    invitations_sent: Mapped[list["Invitation"]] = relationship(
        "Invitation",
        foreign_keys="[Invitation.inviter_id]",
        back_populates="inviter",
        cascade="delete, delete-orphan",
    )
    invitations_received: Mapped[list["Invitation"]] = relationship(
        "Invitation",
        foreign_keys="[Invitation.invitee_id]",
        back_populates="invitee",
        cascade="delete, delete-orphan",
    )

    def __repr__(self) -> str:
        return f"User(id={self.id!r}, username={self.username!r}, email ={self.email!r}, password = {self.password!r}, activated={self.activated!r}, activation_date = {self.activation_date!r}, activation_code = {self.activation_code!r})"


class Home(Base):
    """
    Home model that represents a home owned by a user.

    Attributes:
        id (int): Unique identifier for the home.
        home_name (str): Name of the home (up to 30 characters).
        owned_by (int): The user ID who owns the home (ForeignKey to User).
        owner (User): The owner of the home (one-to-one relationship with User).
        users (list[UserHome]): A list of users belonging to the home with roles (many-to-many relationship with UserHome).
        rooms (list[Room]): A list of rooms in the home (one-to-many relationship with Room).
        tags (list[Tag]): A list of tags associated with the home (one-to-many relationship with Tag).
    """
    __tablename__ = "home"

    id: Mapped[int] = mapped_column(primary_key=True)
    home_name: Mapped[str] = mapped_column(String(30))
    owned_by: Mapped[int] = mapped_column(ForeignKey("user.id"))
    owner: Mapped["User"] = relationship(back_populates="homes")
    users: Mapped[list["UserHome"]] = relationship(
        "UserHome", back_populates="home", cascade="delete, delete-orphan"
    )
    rooms: Mapped[list["Room"]] = relationship(
        "Room", back_populates="home", cascade="delete, delete-orphan"
    )
    tags: Mapped[list["Tag"]] = relationship(
        "Tag", back_populates="home", cascade="delete, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"Home(id={self.id!r}, home_name={self.home_name!r}, owned_by ={self.owned_by!r})"


class UserRole(PyEnum):
    """
    Enum that defines the roles a user can have in a home.

    Values:
        OWNER: User who owns the home.
        MEMBER: User who is a member of the home.
    """
    OWNER = "OWNER"
    MEMBER = "MEMBER"


class UserHome(Base):
    """
    Association model representing a many-to-many relationship between users and homes, including the role of each user in the home.

    Attributes:
        user_id (int): The user ID (ForeignKey to User).
        home_id (int): The home ID (ForeignKey to Home).
        role (str): The role of the user in the home (either 'OWNER' or 'MEMBER').
        user (User): The user associated with the home (one-to-many relationship with User).
        home (Home): The home associated with the user (one-to-many relationship with Home).
    """
    __tablename__ = "users_homes"

    user_id: Mapped[int] = mapped_column(ForeignKey("user.id"), primary_key=True)
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"), primary_key=True)
    role: Mapped[str] = mapped_column(Enum("OWNER", "MEMBER"), nullable=False)
    user = relationship("User", back_populates="user_homes")
    home = relationship("Home", back_populates="users")


class Invitation(Base):
    """
    Invitation model that represents an invitation sent from one user to another for joining a home.

    Attributes:
        id (int): Unique identifier for the invitation.
        inviter_id (int): The user ID of the inviter (ForeignKey to User).
        invitee_id (int): The user ID of the invitee (ForeignKey to User).
        home_id (int): The home ID to which the invitee is being invited (ForeignKey to Home).
        inviter (User): The user who sent the invitation (one-to-many relationship with User).
        invitee (User): The user who received the invitation (one-to-many relationship with User).
    """
    __tablename__ = "invitation"

    id: Mapped[int] = mapped_column(primary_key=True)
    inviter_id: Mapped[int] = mapped_column(ForeignKey("user.id"))
    invitee_id: Mapped[int] = mapped_column(ForeignKey("user.id"))
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"))
    inviter: Mapped["User"] = relationship(
        "User", foreign_keys=[inviter_id], back_populates="invitations_sent"
    )
    invitee: Mapped["User"] = relationship(
        "User", foreign_keys=[invitee_id], back_populates="invitations_received"
    )

    def __repr__(self) -> str:
        return (
            f"Invitation(id={self.id!r}, inviter_id={self.inviter_id!r}, "
            f"invitee_id={self.invitee_id!r}"
        )

    def to_dict(self):
        return {
            "id": self.id,
            "inviter_id": self.inviter_id,
            "invitee_id": self.invitee_id,
            "home_id": self.home_id,
        }


class Room(Base):
    """
    Room model that represents a room in a home.

    Attributes:
        id (int): Unique identifier for the room.
        room_name (str): Name of the room (up to 30 characters).
        home_id (int): The home ID to which the room belongs (ForeignKey to Home).
        home (Home): The home to which the room belongs (one-to-many relationship with Home).
        furnitures (list[Furniture]): A list of furniture items in the room (one-to-many relationship with Furniture).
    """
    __tablename__ = "room"

    id: Mapped[int] = mapped_column(primary_key=True)
    room_name: Mapped[str] = mapped_column(String(30))
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"))
    home: Mapped["Home"] = relationship("Home", back_populates="rooms")
    furnitures: Mapped[list["Furniture"]] = relationship(
        "Furniture", back_populates="room", cascade="delete, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"Room(room_id={self.id!r}, room_name={self.room_name!r}, home_id={self.home_id!r})"


class Furniture(Base):
    """
    Furniture model that represents a furniture item in a room.

    Attributes:
        id (int): Unique identifier for the furniture item.
        furn_name (str): Name of the furniture (up to 50 characters).
        room_id (int): The room ID to which the furniture belongs (ForeignKey to Room).
        room (Room): The room to which the furniture belongs (one-to-many relationship with Room).
        compartments (list[Compartment]): A list of compartments within the furniture (one-to-many relationship with Compartment).
    """
    __tablename__ = "furniture"

    id: Mapped[int] = mapped_column(primary_key=True)
    furn_name: Mapped[str] = mapped_column(String(50), nullable=False)
    room_id: Mapped[int] = mapped_column(ForeignKey("room.id"), nullable=False)
    room: Mapped["Room"] = relationship("Room", back_populates="furnitures")
    compartments: Mapped[list["Compartment"]] = relationship(
        "Compartment", back_populates="furniture", cascade="delete, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"Furniture(id={self.id!r}, furn_name={self.furn_name!r}, room_id={self.room_id!r})"


class Compartment(Base):
    """
    Compartment model that represents a compartment within a piece of furniture.

    Attributes:
        id (int): Unique identifier for the compartment.
        comp_name (str): Name of the compartment (up to 50 characters).
        furn_id (int): The furniture ID to which the compartment belongs (ForeignKey to Furniture).
        furniture (Furniture): The furniture to which the compartment belongs (one-to-many relationship with Furniture).
        items (list[Item]): A list of items stored in the compartment (one-to-many relationship with Item).
    """
    __tablename__ = "compartment"

    id: Mapped[int] = mapped_column(primary_key=True)
    comp_name: Mapped[str] = mapped_column(String(50), nullable=False)
    furn_id: Mapped[int] = mapped_column(ForeignKey("furniture.id"), nullable=False)
    furniture: Mapped["Furniture"] = relationship(
        "Furniture", back_populates="compartments"
    )
    items: Mapped[list["Item"]] = relationship(
        "Item", back_populates="compartment", cascade="delete, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"Compartment(id={self.id!r}, comp_name={self.comp_name!r}, furn_id={self.furn_id!r})"


# Association table for many-to-many relationship between Items and Tags
item_tag_association = Table(
    "items_tags",
    Base.metadata,
    Column("item_id", Integer, ForeignKey("item.id"), primary_key=True),
    Column("tag_id", Integer, ForeignKey("tag.id"), primary_key=True),
)


class Item(Base):
    """
    Item model that represents an item stored in a compartment.

    Attributes:
        id (int): Unique identifier for the item.
        item_name (str): Name of the item (up to 50 characters).
        description (str): Description of the item (optional).
        comp_id (int): The compartment ID to which the item belongs (ForeignKey to Compartment).
        compartment (Compartment): The compartment to which the item belongs (one-to-many relationship with Compartment).
        tags (list[Tag]): A list of tags associated with the item (many-to-many relationship with Tag).
    """
    __tablename__ = "item"

    id: Mapped[int] = mapped_column(primary_key=True)
    item_name: Mapped[str] = mapped_column(String(50), nullable=False)
    comp_id: Mapped[int] = mapped_column(ForeignKey("compartment.id"), nullable=False)
    compartment: Mapped["Compartment"] = relationship(
        "Compartment", back_populates="items"
    )
    tags: Mapped[list["Tag"]] = relationship(
        "Tag",
        secondary=item_tag_association,
        back_populates="items",
        cascade="delete, save-update",
        lazy="selectin",
    )

    def __repr__(self) -> str:
        return f"Item(id={self.id!r}, item_name={self.item_name!r}, comp_id={self.comp_id!r})"


class Tag(Base):
    """
    Tag model that represents a tag that can be associated with multiple items inside a home.

    Attributes:
        id (int): Unique identifier for the tag.
        tag_name (str): Name of the tag (up to 50 characters).
        home_id (int): The home ID to which the tag is associated (ForeignKey to Home).
        home (Home): The home to which the tag is associated (one-to-many relationship with Home).
        items (list[Item]): A list of items associated with the tag (many-to-many relationship with Item).
    """
    __tablename__ = "tag"

    id: Mapped[int] = mapped_column(primary_key=True)
    tag_name: Mapped[str] = mapped_column(String(30), nullable=False)
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"), nullable=False)
    home: Mapped["Home"] = relationship("Home", back_populates="tags")
    items: Mapped[list["Item"]] = relationship(
        "Item", secondary=item_tag_association, back_populates="tags", lazy="selectin"
    )

    def __repr__(self) -> str:
        return (
            f"Tag(id={self.id!r}, tag_name={self.tag_name!r}, home_id={self.home_id!r})"
        )
