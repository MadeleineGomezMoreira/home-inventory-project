from pydantic import BaseModel
from typing import List


class HomeBase(BaseModel):
    home_name: str

    class Config:
        orm_mode = True


class HomeCreate(HomeBase):
    pass  # No se requiere ningún campo adicional para crear un hogar


class HomeResponse(HomeBase):
    id: int
    owned_by: int  # Mostrará el ID del usuario que posee este hogar

    class Config:
        orm_mode = True
