from app.models.models import Home
from app.schemas.home import HomeCreate


def map_homeCreate_to_home(home_create: HomeCreate) -> Home:

    return Home(home_name=home_create.home_name, owned_by=home_create.owned_by)
