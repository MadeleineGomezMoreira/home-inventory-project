from app.models.models import (
    User,
    UserHome,
)
import bcrypt
from app.schemas.user import UserCreate, UserResponse, UsersByRoleResponse, UserRequestLogin
from app.mappers.user_mapper import map_userCreate_to_user
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, delete
from app.exceptions.custom_exceptions import(
    ActivationCodeExpiredException,
    InvalidActivationCodeException,
    UserNotFoundException, 
    UserNotActivatedException, 
    AccountAlreadyActivatedException, 
    MailMessagingException,
    UserUpdatingException,
    WrongUserCredentialsException,
    SavingUserException,
) 
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from urllib.parse import unquote
import urllib.parse
import smtplib
import os
import secrets
import base64
import datetime
from datetime import timedelta

EMAIL_ADDRESS = os.getenv('EMAIL_ADDRESS', "alumnosdamquevedo@gmail.com")
EMAIL_PASSWORD = os.getenv('EMAIL_PASSWORD', "uyhqfbbfmszvuykt")

# Constants (replace these with actual values)
CLICK_LINK_TO_ACTIVATE_ACCOUNT = "Click the link to activate your account: "
ACTIVATE_ACCOUNT_LINK = "https://informatica.iesquevedo.es/home-inventory/activate"
EMAIL_URL = "?email="
CODE_URL = "&code="
ACTIVATE_YOUR_ACCOUNT_MAIL_SUBJECT = "Activate Your Account"
FAILED_TO_SEND_EMAIL_ERROR = "Failed to send email."

async def save_user(session: AsyncSession, user: UserCreate):
    """
    Saves a new user in the database with hashed password and sends an activation email.
    - Hashes the password and stores it in the database.
    - Sends an email with an activation link to the user's email.
    
    Args:
        session (`AsyncSession`) - The database session to interact with the database.
        user (`UserCreate`) - The schema object containing user information for creation.

    Returns:
        `User`: The newly created user object.

    Raises:
        `MailMessagingException`: If there's an issue sending the activation email.
    """
    try: 
        hashed_password = bcrypt.hashpw(user.password.encode('utf-8'), bcrypt.gensalt())

        mapped_user = map_userCreate_to_user(user)
    
        mapped_user.password = hashed_password.decode('utf-8')
        session.add(mapped_user)

        await send_email(session, user.email)

        await session.commit()
        await session.refresh(mapped_user)
    

        return mapped_user
    except Exception as e:
        await session.rollback()
        raise SavingUserException(e) from e


async def get_all_users_by_home_by_role(session: AsyncSession, home_id: int):
    """
    Retrieves all users from a specific home, grouped by their role (OWNER or MEMBER).

    Args:
        session (`AsyncSession`) - The database session to interact with the database.
        home_id (`int`): The ID of the home to fetch users for.

    Returns:
        `UsersByRoleResponse`: A response object containing the users, grouped by role.

    Raises:
        `UserNotFoundException`: If no users are found for the given home ID.
    """
    stmt = (
        select(User, UserHome.role)
        .join(UserHome)
        .filter(
            UserHome.home_id == home_id,
        )
    )
    result = await session.execute(stmt)

    users_by_role = {"OWNER": None, "MEMBER": []}

    for user, role in result.all():
        user_response = UserResponse(
            id=user.id,
            username=user.username,
            email=user.email,
        )
        if role == "OWNER":
            users_by_role["OWNER"] = user_response
        elif role == "MEMBER":
            users_by_role["MEMBER"].append(user_response)

    return UsersByRoleResponse(
        OWNER=users_by_role["OWNER"], MEMBER=users_by_role["MEMBER"]
    )

async def delete_user(session: AsyncSession, user_id: int):
    """
    Deletes a user from the database, along with their associated furniture and compartments.

    Args:
        session (`AsyncSession`) - The database session to interact with the database.
        user_id (`int`) - The ID of the user to delete.

    Raises:
        `UserNotFoundException`: If no user is found for the given ID.
    """
    user = await session.get(User, user_id)

    if not user:
        raise UserNotFoundException()

    await session.execute(delete(User).where(User.id == user_id))
    await session.commit()


async def update_user(session: AsyncSession, user: UserCreate):
    """
    Updates an existing user's information in the database.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        user: `UserCreate` - The schema object containing user information for update.

    Raises:
        UserUpdatingException: If an error occurs while updating the user.
    """
    try:
        mapped_user = map_userCreate_to_user(user)
        session.add(mapped_user)
        await session.commit()
    except Exception as e:
        await session.rollback()
        raise UserUpdatingException(e) from e


async def get_all_users(session: AsyncSession):
    """
    Retrieves all users from the database.

    Args:
        session (`AsyncSession`): The database session to interact with the database.

    Returns:
        List[`User`]: A list of all user objects in the database.
    """
    result = await session.execute(select(User))
    return result.scalars().all()


async def get_user_by_id(session: AsyncSession, user_id: int):
    """
    Retrieves a user from the database by their ID.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        user_id (`int`): The ID of the user to fetch.

    Returns:
        `User`: The user object with the given ID.

    Raises:
        `UserNotFoundException`: If no user is found for the given ID.
    """
    stmt = select(User).where(User.id == user_id)
    result = await session.execute(stmt)
    user = result.scalar_one_or_none()
    if not user:
        raise UserNotFoundException()
    return user


async def get_user_by_username(session: AsyncSession, username: str):
    """
    Retrieves a user from the database by their username.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        username (`str`): The username of the user to fetch.

    Returns:
        `User`: The user object with the given username.

    Raises:
        `UserNotFoundException`: If no user is found for the given username.
    """
    stmt = select(User).where(User.username == username)
    result = await session.execute(stmt)
    user = result.scalar_one_or_none()
    if not user:
        raise UserNotFoundException()
    return user

async def get_user_by_email(session: AsyncSession, email: str):
    """
    Retrieves a user from the database by their email address.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        email (`str`): The email address of the user to fetch.

    Returns:
        `User`: The user object with the given email.

    Raises:
        `UserNotFoundException`: If no user is found for the given email.
    """
    stmt = select(User).where(User.email == email)
    result = await session.execute(stmt)
    user = result.scalars().first()
    if not user:
        raise UserNotFoundException()
    return user

async def activate_user_account(session: AsyncSession, email: str, activation_code: str):
    """
    Activates a user's account using the provided activation code.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        email (`str`): The email address of the user to activate.
        activation_code (`str`): The activation code provided for account activation.

    Returns:
        `bool`: True if the account was successfully activated.

    Raises:
        `ActivationCodeExpiredException`: If the activation code has expired.
        `InvalidActivationCodeException`: If the provided activation code is invalid.
        `UserNotFoundException`: If the user cannot be found for the given email.
    """
    user = await get_user_by_email(session, email)
    
    if not user:
        raise UserNotFoundException(f"User with email {email} not found")

    if user.activated:
        raise AccountAlreadyActivatedException(f"User with email {email} is already activated")

    decoded_activation_code = unquote(activation_code)
    stored_activation_code = unquote(user.activation_code)
    
    activation_date = user.activation_date + datetime.timedelta(hours=2)
    expiration_time = activation_date + datetime.timedelta(minutes=5)
    
    if expiration_time < datetime.datetime.now():
        await send_email(session, email)
        raise ActivationCodeExpiredException("The code used to activate the account is expired. A new link was just sent to your email")
    
    if decoded_activation_code == stored_activation_code:
        user.activated = True
        await session.commit()
        return True
    
    raise InvalidActivationCodeException()

async def login_by_user_password(session: AsyncSession, user_login: UserRequestLogin):
    """
    Authenticates a user using their username and password.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        user_login (`UserRequestLogin`): The login request containing username and password.

    Returns:
        int: The user ID if login is successful.

    Raises:
        `UserNotFoundException`: If no user is found for the given username.
        `UserNotActivatedException`: If the user's account is not activated.
        `WrongUserCredentialsException`: If the provided password is incorrect.
    """
    user = await get_user_by_username(session, user_login.username)
    if not user:
        raise UserNotFoundException()
    if(user.activated == False):
        raise UserNotActivatedException()
    elif(verify_password(user.password, user_login.password)):
        return user.id
    else:
        raise WrongUserCredentialsException()

async def update_activation_code(session: AsyncSession, recipient: str, activation_code: str):
    """
    Updates the activation code for a user.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        recipient (`str`): The email address of the user to update.
        activation_code (`str`): The new activation code to set for the user.

    Raises:
        `UserNotFoundException`: If no user is found for the given email address.

    Returns:
        `str`: The updated activation code.
    """
    user = await get_user_by_email(session, recipient)
    if not user:
        raise UserNotFoundException()
    user.activation_code = activation_code 
    session.add(user)
    await session.commit()
    await session.refresh(user)
    return user.activation_code

def verify_password(stored_hash: str, input_password: str) -> bool:
    """
    Verifies if the provided password matches the stored hash.

    Args:
        stored_hash (`str`): The stored hashed password.
        input_password (`str`): The input password to verify.

    Returns:
        `bool`: True if the passwords match, False otherwise.
    """
    return bcrypt.checkpw(input_password.encode('utf-8'), stored_hash.encode('utf-8'))

def generate_activation_code() -> str:
    """
    Generates a new random activation code.

    Returns:
        `str`: The generated activation code.
    """
    random_bytes = secrets.token_bytes(32)
    encoded_activation_code = base64.b64encode(random_bytes).decode('utf-8')
    url_encoded_activation_code = urllib.parse.quote(encoded_activation_code, safe='')
    return url_encoded_activation_code

async def send_email(session: AsyncSession, recipient: str):
    """
    Sends an activation email to the user with a generated activation code.

    Args:
        session (`AsyncSession`): The database session to interact with the database.
        recipient (`str`): The email address of the user to send the activation email to.

    Raises:
        `MailMessagingException`: If there's an issue sending the activation email.
    """
    try:
        activation_code = generate_activation_code()
        new_code = await update_activation_code(session, recipient, activation_code)

        message = f"{CLICK_LINK_TO_ACTIVATE_ACCOUNT} {ACTIVATE_ACCOUNT_LINK}{EMAIL_URL}{recipient}{CODE_URL}{new_code}"

        sender_email = EMAIL_ADDRESS
        receiver_email = recipient
        password = EMAIL_PASSWORD

        msg = MIMEMultipart()
        msg['From'] = sender_email
        msg['To'] = receiver_email
        msg['Subject'] = ACTIVATE_YOUR_ACCOUNT_MAIL_SUBJECT
        msg.attach(MIMEText(message, 'plain'))

        try:
            with smtplib.SMTP('smtp.gmail.com', 587) as server:
                server.starttls()
                server.login(sender_email, password)
                server.sendmail(sender_email, receiver_email, msg.as_string())
        except Exception as e:
            raise MailMessagingException(e) from e

    except Exception as e:
        raise MailMessagingException(e) from e