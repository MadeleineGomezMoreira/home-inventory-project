class RoomNotFoundError(Exception):
    """Raised when a room is not found in the database."""

    pass


class HomeNotFoundError(Exception):
    """Raised when a home is not found in the database."""

    pass


class UserNotFoundError(Exception):
    """Raised when a user is not found in the database."""

    pass


class UserAlreadyInHomeException(Exception):
    """Raised when a user is already found in the home when trying to invite them to that home."""

    pass


class InvitationAlreadyExistsException(Exception):
    """Raised when an invitation to that user to join a specific home is already found in the database."""

    pass


class InvitationNotFoundError(Exception):
    """Raised when a user is not found in the database."""

    pass


class SavingRoomError(Exception):
    """Raised when there is an error during the action of saving a user into the database."""

    def __init__(self, message="Failed to save room"):
        super().__init__(message)


class DatabaseError(Exception):
    """Raised when there is an error during commit to the database."""

    pass
