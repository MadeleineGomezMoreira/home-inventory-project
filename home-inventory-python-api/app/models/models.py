from sqlalchemy import ForeignKey, String, Boolean, DateTime, Enum
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship
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
    activation_code: Mapped[str] = mapped_column(String(45), default="N/A")

    # Here we will define the relationship with Home (one user can own multiple homes)
    homes: Mapped[list["Home"]] = relationship(back_populates="owner")
    user_homes: Mapped[list["UserHome"]] = relationship(
        "UserHome", back_populates="user"
    )
    invitations_sent: Mapped[list["Invitation"]] = relationship(
        "Invitation", foreign_keys="[Invitation.inviter_id]", back_populates="inviter"
    )
    invitations_received: Mapped[list["Invitation"]] = relationship(
        "Invitation", foreign_keys="[Invitation.invitee_id]", back_populates="invitee"
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
    users: Mapped[list["UserHome"]] = relationship("UserHome", back_populates="home")
    rooms: Mapped[list["Room"]] = relationship("Room", back_populates="home")

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
        "Furniture", back_populates="room"
    )

    def __repr__(self) -> str:
        return f"Room(room_id={self.room_id!r}, room_name={self.room_name!r}, home_id={self.home_id!r})"


class Furniture(Base):
    __tablename__ = "furniture"

    # Define the primary key with auto-increment
    id: Mapped[int] = mapped_column(primary_key=True)
    furn_name: Mapped[str] = mapped_column(String(50), nullable=False)

    # Foreign key pointing to the Room table
    room_id: Mapped[int] = mapped_column(ForeignKey("room.id"), nullable=False)

    # Relationship back to Room
    room: Mapped["Room"] = relationship("Room", back_populates="furnitures")

    def __repr__(self) -> str:
        return f"Furniture(id={self.id!r}, furn_name={self.furn_name!r}, room_id={self.room_id!r})"
