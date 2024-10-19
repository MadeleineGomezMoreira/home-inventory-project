from pydantic import BaseModel
from datetime import datetime
from typing import List, Optional


class UserBase(BaseModel):
    username: str
    password: str
    email: str
    activated: Optional[bool] = False
    activation_date: Optional[datetime] = None
    activation_code: Optional[str] = None

    class Config:
        orm_mode = (
            True  # Esto permite a Pydantic leer datos desde los modelos de SQLAlchemy
        )


class UserCreate(UserBase):
    username: str  # El nombre de usuario debe ser obligatorio en la creación
    password: str  # La contraseña debe ser obligatoria en la creación
    email: str  # La dirección de correo electrónico debe ser obligatorio en la creación


class UserResponse(UserBase):
    id: int  # Este campo será retornado cuando devuelvas un usuario existente
    #    homes: Optional[List["HomeResponse"]] = (
    #        []
    #    )  # Usarás esto cuando devuelvas relaciones con otros objetos

    class Config:
        orm_mode = True
