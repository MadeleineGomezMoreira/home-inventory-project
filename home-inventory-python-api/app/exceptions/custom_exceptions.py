class RoomNotFoundException(Exception):
    """Exception raised when a room is not found in the database.

    This typically occurs during queries or operations attempting to retrieve or interact with a room that does not exist.
    """
    def __init__(self, message="The requested room was not found in the database"):
        super().__init__(message)

class UserNotActivatedException(Exception):
    """Exception raised when trying to log into an unactivated account.

    This prevents users from accessing their account until they have activated it via the activation link in an email.
    """
    def __init__(self, message="Your account is not activated. Please activate your account through the link sent to your email"):
        super().__init__(message)

class AccountAlreadyActivatedException(Exception):
    """Exception raised when attempting to activate an already activated account.

    Ensures that activation attempts are only performed on accounts requiring activation.
    """
    def __init__(self, message="The account you are trying to activate is already activated"):
        super().__init__(message)
    
class MailMessagingException(Exception):
    """Exception raised when there is an error sending the activation email to a user.

    This could be caused by network issues, incorrect email addresses, or server problems.
    """
    def __init__(self, message="There was an error while sending the activation email"):
        super().__init__(message)


class CompartmentNotFoundException(Exception):
    """Exception raised when a compartment is not found in the database.

        This typically occurs during queries or updates targeting a compartment record that does not exist.
    """
    def __init__(self, message="The requested compartment was not found in the database"):
        super().__init__(message)


class ItemNotFoundException(Exception):
    """Exception raised when an item is not found in the database.

    This typically occurs during queries or updates targeting an item record that does not exist.
    """
    def __init__(self, message="The requested item was not found in the database"):
        super().__init__(message)


class HomeNotFoundException(Exception):
    """Exception raised when a home is not found in the database.

    This typically occurs during queries or updates targeting a home record that does not exist.
    """
    def __init__(self, message="The requested home was not found in the database"):
        super().__init__(message)

class FurnitureNotFoundException(Exception):
    """Exception raised when a piece of furniture is not found in the database.

    This typically occurs during queries or updates targeting a furniture record that does not exist.
    """
    def __init__(self, message="The requested piece of furniture was not found in the database"):
        super().__init__(message)

class WrongUserCredentialsException(Exception):
    """Exception raised when incorrect credentials are used during login.

    This typically occurs when the entered password does not match the stored password for a user.
    """
    def __init__(self, message="The password is incorrect. Please try again."):
        super().__init__(message)

class ActivationCodeExpiredException(Exception):
    """Exception raised when the account activation code has expired.

    Users encountering this should request a new activation code to proceed.
    """
    def __init__(self, message="The activation code is expired. Please request a new one"):
        super().__init__(message)

class InvalidActivationCodeException(Exception):
    """Exception raised when an account activation code is invalid.

    This prevents users from activating accounts with invalid or tampered codes.
    
    Users encountering this should request a new activation code to proceed.
    """
    def __init__(self, message="The activation code is not valid. Please request a new one"):
        super().__init__(message)

class UserNotFoundException(Exception):
    """Exception raised when a user is not found in the database.

    This typically occurs during queries or updates targeting a user record that does not exist.
    """
    def __init__(self, message="The requested user was not found in the database"):
        super().__init__(message)


class UserAlreadyInHomeException(Exception):
    """Raised when a user is already found in the home when trying to invite them to that home.

    This exception is used to prevent duplicate invitations or errors during the invitation process.

    Example:
        If the user with `id=2` is already in a home with `id=3`, raising this exception informs the caller.
    """
    def __init__(self, message="The user is already a member of that home"):
        super().__init__(message)


class UserNotInHomeException(Exception):
    """Exception raised when attempting to remove a user who is not a member of a home.

    Ensures accurate membership management within homes.
    """
    def __init__(self, message="The user you are trying to kick from the home was not found in the home"):
        super().__init__(message)


class InvitationAlreadyExistsException(Exception):
    """Exception raised when an invitation for a user to join a specific home already exists.

    Prevents redundant invitations and ensures a clear invitation process.
    """
    def __init__(self, message="The user was already invited to that home"):
        super().__init__(message)


class InvitationNotFoundException(Exception):
    """Exception raised when an invitation is not found in the database.

    This typically occurs during queries or updates targeting an invitation record that does not exist.
    """
    def __init__(self, message="The requested invitation was not found in the database"):
        super().__init__(message)

class SavingUserException(Exception):
    """Exception raised when there is an error saving a user to the database.

    Indicates potential issues with the database or input data.
    """
    def __init__(self, message="Failed to save user"):
        super().__init__(message)

class SavingHomeException(Exception):
    """Exception raised when there is an error saving a home to the database.

    Indicates potential issues with the database or input data.
    """
    def __init__(self, message="Failed to save home"):
        super().__init__(message)

class SavingRoomException(Exception):
    """Exception raised when there is an error saving a room to the database.

    Indicates potential issues with the database or input data.
    """
    def __init__(self, message="Failed to save room"):
        super().__init__(message)

class SavingFurnitureException(Exception):
    """Exception raised when there is an error saving a piece of furniture to the database.

    Indicates potential issues with the database or input data.
    """
    def __init__(self, message="Failed to save furniture"):
        super().__init__(message)

class SavingCompartmentException(Exception):
    """Exception raised when there is an error saving a compartment to the database.

    Indicates potential issues with the database or input data.
    """
    def __init__(self, message="Failed to save compartment"):
        super().__init__(message)

class SavingItemException(Exception):
    """Exception raised when there is an error saving an item to the database.

    Indicates potential issues with the database or input data.
    """
    def __init__(self, message="Failed to save item"):
        super().__init__(message)

class DatabaseException(Exception):
    """Exception raised for general errors related to database usage.

    This serves as a fallback for issues not covered by specific exceptions.
    """
    def __init__(self, message="There was an error related to database usage"):
        super().__init__(message)

class ItemMovementBetweenHomesNotAllowed(Exception):
    """Exception raised when an item is attempted to be moved between different homes.

    Items are restricted to movement within the same home for consistency.
    """
    def __init__(self, message="Failed to move item to another home. Items must only be moved between homes"):
        super().__init__(message)

class ItemUpdatingException(Exception):
    """Exception raised when there is an error while trying to update an item.

    This may occur while trying to set a name for the item that is too long.
    """
    def __init__(self, message="There was an error while trying to update the item"):
        super().__init__(message)

class CompartmentUpdatingException(Exception):
    """Exception raised when there is an error while trying to update a compartment.

    This may occur while trying to set a name for the compartment that is too long.
    """
    def __init__(self, message="There was an error while trying to update the compartment"):
        super().__init__(message)

class FurnitureUpdatingException(Exception):
    """Exception raised when there is an error while trying to update a piece of furniture.

    This may occur while trying to set a name for the piece of furniture that is too long.
    """
    def __init__(self, message="There was an error while trying to update the piece of furniture"):
        super().__init__(message)

class HomeUpdatingException(Exception):
    """Exception raised when there is an error while trying to update a home.

    This may occur while trying to set a name for the home that is too long.
    """
    def __init__(self, message="There was an error while trying to update the home"):
        super().__init__(message)

class RoomUpdatingException(Exception):
    """Exception raised when there is an error while trying to update a room.

    This may occur while trying to set a name for the room that is too long.
    """
    def __init__(self, message="There was an error while trying to update the room"):
        super().__init__(message)


class UserUpdatingException(Exception):
    """Exception raised when there is an error while trying to update a user.

    This may occur while trying to set a username for the user that is too long or an invalid email.
    """
    def __init__(self, message="There was an error while trying to update the user"):
        super().__init__(message)
        
class HomeDeletionException(Exception):
    """Exception raised when there is an error while trying to delete a home.

    This may occur while trying to delete a home while not being able to delete its rooms, furniture, compartments or items.
    """
    def __init__(self, message="There was an error while trying to delete the home"):
        super().__init__(message)
        
class InvitationAcceptingException(Exception):
    """Exception raised when there is an error while trying to accept an invitation.

    This may occur while trying to accept an invitation from a member or a house that no longer exist.
    """
    def __init__(self, message="There was an error while trying to accept an invitation"):
        super().__init__(message)

class MemberRemovalException(Exception):
    """Exception raised when there is an error while trying remove a member from a home.

    This may occur while trying to remove a member from a home from the database.
    """
    def __init__(self, message="There was an error while trying to remove a member from the home"):
        super().__init__(message)

class UnspecifiedHomeIdException(Exception):
    """Exception raised when failing to find the id for a home that is not valid.

    This tipically occurs while querying a specific compartment related to a non-existent home-id.
    """
    def __init__(self, message="There was an error while trying to update the item"):
        super().__init__(message)
