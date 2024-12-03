from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import List, Optional


class UserBase(BaseModel):
    username: str
    password: str
    email: str
    activated: Optional[bool] = False
    activation_date: Optional[datetime] = None
    activation_code: Optional[str] = None

    model_config = ConfigDict(from_attributes=True)


class UserCreate(UserBase):
    username: str 
    password: str 
    email: str


class UserResponse(BaseModel):
    id: int
    username: str
    email: str

    model_config = ConfigDict(from_attributes=True, extra="ignore")

class UserRequestLogin(BaseModel):
    username: str
    password: str

    model_config = ConfigDict(from_attributes=True)

class UsersByRoleResponse(BaseModel):
    OWNER: UserResponse
    MEMBER: List[UserResponse]

    model_config = ConfigDict(
        populate_by_name=True,
        alias_generator=lambda field: "".join(
            word.capitalize() if i else word for i, word in enumerate(field.split("_"))
        ),
    )
