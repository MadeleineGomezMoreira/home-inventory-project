from sqlalchemy import ForeignKey, String, Boolean, DateTime
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship
import datetime
import enum
from enum import Enum as PyEnum
from app.models.base import Base


class User(Base):
    __tablename__ = "user"

    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)
    username: Mapped[str] = mapped_column(String(30))
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

    def __repr__(self) -> str:
        return f"User(id={self.id!r}, username={self.username!r}, email ={self.email!r}, password = {self.password!r}, activated={self.activated!r}, activation_date = {self.activation_date!r}, activation_code = {self.activation_code!r})"


class Home(Base):
    __tablename__ = "home"

    id: Mapped[int] = mapped_column(primary_key=True)
    home_name: Mapped[str] = mapped_column(String(15))

    # Foreign key pointing to the User table
    owned_by: Mapped[int] = mapped_column(ForeignKey("user.id"))

    # We will define the back-populated relationship to User
    owner: Mapped["User"] = relationship(back_populates="homes")

    def __repr__(self) -> str:
        return f"Home(id={self.id!r}, home_name={self.home_name!r}, owned_by ={self.owned_by!r})"


class UserRole(enum.Enum):
    OWNER = "OWNER"
    MEMBER = "MEMBER"


class UserHome(Base):
    __tablename__ = "users_homes"

    user_id: Mapped[int] = mapped_column(ForeignKey("user.id"), primary_key=True)
    home_id: Mapped[int] = mapped_column(ForeignKey("home.id"), primary_key=True)
    role: Mapped[str] = mapped_column(enum(UserRole), nullable=False)

    # Optional relationship to facilitate data loading
    user = relationship("User", back_populates="homes")
    home = relationship("Home", back_populates="users")


class InvitationStatus(PyEnum):
    PENDING = "PENDING"
    ACCEPTED = "ACCEPTED"
    REJECTED = "REJECTED"


class Invitation(Base):
    __tablename__ = "invitation"

    id: Mapped[int] = mapped_column(primary_key=True)
    inviter_id: Mapped[int] = mapped_column(ForeignKey("user.id"))
    invitee_id: Mapped[int] = mapped_column(ForeignKey("user.id"))
    status: Mapped[str] = mapped_column(
        enum(InvitationStatus), default=InvitationStatus.PENDING
    )
