class RoomNotFoundError(Exception):
    """Raised when a room is not found in the database."""

    pass


class HomeNotFoundError(Exception):
    """Raised when a home is not found in the database."""

    pass


class UserNotFoundError(Exception):
    """Raised when a user is not found in the database."""

    pass


class SavingRoomError(Exception):
    """Raised when there is an error during the action of saving a user into the database."""

    def __init__(self, message="Failed to save room"):
        super().__init__(message)


class DatabaseError(Exception):
    """Raised when there is an error during commit to the database."""

    pass
