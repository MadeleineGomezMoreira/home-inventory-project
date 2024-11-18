from fastapi import APIRouter
from app.routes.users import router as users_router
from app.routes.homes import router as homes_router
from app.routes.rooms import router as rooms_router
from app.routes.invitations import router as invitations_router
from app.routes.furnitures import router as furnitures_router
from app.routes.compartments import router as compartments_router

api_router = APIRouter()

# Includes routers from different modules
api_router.include_router(users_router, tags=["users"])
api_router.include_router(homes_router, tags=["homes"])
api_router.include_router(rooms_router, tags=["rooms"])
api_router.include_router(invitations_router, tags=["invitations"])
api_router.include_router(furnitures_router, tags=["furnitures"])
api_router.include_router(compartments_router, tags=["compartments"])
