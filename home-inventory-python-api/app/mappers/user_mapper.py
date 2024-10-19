from app.models.models import User
from app.schemas.user import UserCreate
import datetime


def map_userCreate_to_user(user_create: UserCreate) -> User:

    return User(
        username=user_create.username,
        email=user_create.email,
        password=user_create.password,
        activated=user_create.activated if user_create.activated is not None else False,
        activation_date=(
            user_create.activation_date
            if user_create.activation_date is not None
            else datetime.datetime.now()
        ),
        activation_code=(
            user_create.activation_code
            if user_create.activation_code is not None
            else "N/A"
        ),
    )
