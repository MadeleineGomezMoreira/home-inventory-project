from fastapi import APIRouter
from app.routes.users import router as users_router
from app.routes.homes import router as homes_router
from app.routes.rooms import router as rooms_router

api_router = APIRouter()

# Includes routers from different modules
api_router.include_router(users_router, tags=["users"])
api_router.include_router(homes_router, tags=["homes"])
api_router.include_router(rooms_router, tags=["rooms"])
