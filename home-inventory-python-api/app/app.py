from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from app.exceptions.custom_exceptions import SavingRoomError, HomeNotFoundError
from app.routes import api_router

app = FastAPI()

app.include_router(api_router)

# Exception handling


@app.exception_handler(SavingRoomError)
async def saving_room_error_handler(request: Request, exception: SavingRoomError):
    # Return a JSON response with a 400 Bad Request status code
    return JSONResponse(
        status_code=400,
        content={"detail": str(exception)},
    )


@app.exception_handler(HomeNotFoundError)
async def saving_room_error_handler(request: Request, exception: HomeNotFoundError):
    # Return a JSON response with a 404 Not Found status code
    return JSONResponse(
        status_code=404,
        content={"detail": str(exception)},
    )
