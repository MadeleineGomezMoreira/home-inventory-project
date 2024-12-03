from tkinter import CASCADE
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
    __tablename__ = "user"

    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)
    username: Mapped[str] = mapped_column(String(30), unique=True)
    email: Mapped[str] = mapped_column(String(100))
    password: Mapped[str] = mapped_column(String(100))

    # I'll set default values for activated, activation_date, and activation_code
    activated: Mapped[bool] = mapped_column(Boolean, default=False)
    activation_date: Mapped[datetime.datetime] = mapped_column(
        DateTime, default=datetime.datetime.now
    )
    activation_code: Mapped[str] = mapped_column(String(512), default="N/A")

    # Here we will define the relationship with Home (one user can own multiple homes)
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
    __tablename__ = "home"

    id: Mapped[int] = mapped_column(primary_key=True)
    home_name: Mapped[str] = mapped_column(String(30))

    # Foreign key pointing to the User table
    owned_by: Mapped[int] = mapped_column(ForeignKey("user.id"))

    # We will define the back-populated relationship to User
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
    OWNER = "OWNER"
    MEMBER = "MEMBER"


class UserHome(Base):
    __tablename__ = "users_homes"

    user_id: Mapped[int] = mapped_column(ForeignKey("user.id"), primary_key=True)
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"), primary_key=True)
    role: Mapped[str] = mapped_column(Enum("OWNER", "MEMBER"), nullable=False)

    # Optional relationship to facilitate data loading
    user = relationship("User", back_populates="user_homes")
    home = relationship("Home", back_populates="users")


class Invitation(Base):
    __tablename__ = "invitation"

    id: Mapped[int] = mapped_column(primary_key=True)
    inviter_id: Mapped[int] = mapped_column(ForeignKey("user.id"))
    invitee_id: Mapped[int] = mapped_column(ForeignKey("user.id"))
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"))

    # Relationships
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
    __tablename__ = "room"

    # Define the primary key with auto-increment
    id: Mapped[int] = mapped_column(primary_key=True)
    room_name: Mapped[str] = mapped_column(String(30))

    # Foreign key pointing to the Home table
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"))

    # Relationship back to Home
    home: Mapped["Home"] = relationship("Home", back_populates="rooms")

    # Relationship back to Furniture
    furnitures: Mapped[list["Furniture"]] = relationship(
        "Furniture", back_populates="room", cascade="delete, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"Room(room_id={self.id!r}, room_name={self.room_name!r}, home_id={self.home_id!r})"


class Furniture(Base):
    __tablename__ = "furniture"

    # Define the primary key with auto-increment
    id: Mapped[int] = mapped_column(primary_key=True)
    furn_name: Mapped[str] = mapped_column(String(50), nullable=False)

    # Foreign key pointing to the Room table
    room_id: Mapped[int] = mapped_column(ForeignKey("room.id"), nullable=False)

    # Relationship back to Room
    room: Mapped["Room"] = relationship("Room", back_populates="furnitures")

    # Relationship back to Compartment (one furniture can have multiple compartments)
    compartments: Mapped[list["Compartment"]] = relationship(
        "Compartment", back_populates="furniture", cascade="delete, delete-orphan"
    )

    def __repr__(self) -> str:
        return f"Furniture(id={self.id!r}, furn_name={self.furn_name!r}, room_id={self.room_id!r})"


class Compartment(Base):
    __tablename__ = "compartment"

    # Define the primary key for compartment
    id: Mapped[int] = mapped_column(primary_key=True)
    comp_name: Mapped[str] = mapped_column(String(50), nullable=False)

    # Foreign key pointing to the Furniture table
    furn_id: Mapped[int] = mapped_column(ForeignKey("furniture.id"), nullable=False)

    # Relationship back to Furniture
    furniture: Mapped["Furniture"] = relationship(
        "Furniture", back_populates="compartments"
    )

    # Relationship to Item
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
    __tablename__ = "item"

    # Define the primary key with auto-increment
    id: Mapped[int] = mapped_column(primary_key=True)
    item_name: Mapped[str] = mapped_column(String(50), nullable=False)

    # Foreign key pointing to the Compartment table
    comp_id: Mapped[int] = mapped_column(ForeignKey("compartment.id"), nullable=False)

    # Relationship back to Compartment
    compartment: Mapped["Compartment"] = relationship(
        "Compartment", back_populates="items"
    )

    # Many-to-many relationship with Tag
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
    __tablename__ = "tag"

    # Define the primary key with auto-increment
    id: Mapped[int] = mapped_column(primary_key=True)
    tag_name: Mapped[str] = mapped_column(String(30), unique=True, nullable=False)

    # Add home_id as a foreign key pointing to the Home table
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"), nullable=False)

    # Relationship back to Home
    home: Mapped["Home"] = relationship("Home", back_populates="tags")

    # Many-to-many relationship with Item
    items: Mapped[list["Item"]] = relationship(
        "Item", secondary=item_tag_association, back_populates="tags", lazy="selectin"
    )

    def __repr__(self) -> str:
        return (
            f"Tag(id={self.id!r}, tag_name={self.tag_name!r}, home_id={self.home_id!r})"
        )
