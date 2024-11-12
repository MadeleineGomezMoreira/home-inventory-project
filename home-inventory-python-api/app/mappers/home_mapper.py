from app.models.models import Home
from app.schemas.home import HomeCreate, HomeRequest


def map_homeCreate_to_home(home_create: HomeCreate) -> Home:

    return Home(home_name=home_create.home_name, owned_by=home_create.owned_by)


def map_homeRequest_to_home(home_request: HomeRequest) -> Home:

    return Home(
        id=home_request.id,
        home_name=home_request.home_name,
    )
