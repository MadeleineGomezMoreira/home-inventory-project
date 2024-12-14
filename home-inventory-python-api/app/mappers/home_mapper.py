from app.models.models import Home
from app.schemas.home import HomeCreate, HomeRequest


def map_homeCreate_to_home(home_create: HomeCreate) -> Home:
    """Maps a HomeCreate schema object to a Home model object.

    This function converts data from the `HomeCreate` Pydantic schema, which is used for user input validation,
    into a `Home` model object that can be used for database operations, typically during the creation of a new home.

    Args:
        home_create (HomeCreate): The input schema object containing the home data for creation.

    Returns:
        Home: The `Home` model populated with the data from the `HomeCreate` schema.
    """
    return Home(home_name=home_create.home_name, owned_by=home_create.owned_by)


def map_homeRequest_to_home(home_request: HomeRequest) -> Home:
    """Maps a HomeRequest schema object to a Home model object.

    This function converts data from the `HomeRequest` Pydantic schema, which is typically used for handling 
    home updates or retrieving home data, into a `Home` model object that can be used for database operations.

    Args:
        home_request (HomeRequest): The input schema object containing the home data to update or fetch.

    Returns:
        Home: The `Home` model populated with the data from the `HomeRequest` schema.
    """
    return Home(
        id=home_request.id,
        home_name=home_request.home_name,
    )
