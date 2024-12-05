from app.models.models import (
    User,
    UserHome,
    Furniture,
    Invitation,
    Home,
    Room,
    Compartment,
)
import bcrypt
from app.schemas.user import UserCreate, UserResponse, UsersByRoleResponse, UserRequestLogin
from app.mappers.user_mapper import map_userCreate_to_user
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select, delete
from app.exceptions.custom_exceptions import(
    ActivationCodeExpiredError,
    InvalidActivationCodeError,
    UserNotFoundError, 
    UserNotActivatedError, 
    AccountAlreadyActivatedError, 
    MailMessagingException
) 
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
from urllib.parse import unquote
import urllib.parse
import smtplib
import os
import secrets
import base64
import datetime
from datetime import timedelta

EMAIL_ADDRESS = os.getenv('EMAIL_ADDRESS')
EMAIL_PASSWORD = os.getenv('EMAIL_PASSWORD')

# Constants (replace these with actual values)
CLICK_LINK_TO_ACTIVATE_ACCOUNT = "Click the link to activate your account: "
ACTIVATE_ACCOUNT_LINK = "http://192.168.1.232:8085/activate"
EMAIL_URL = "?email="
CODE_URL = "&code="
ACTIVATE_YOUR_ACCOUNT_MAIL_SUBJECT = "Activate Your Account"
FAILED_TO_SEND_EMAIL_ERROR = "Failed to send email."


# Save a new user
async def save_user(session: AsyncSession, user: UserCreate):
    hashed_password = bcrypt.hashpw(user.password.encode('utf-8'), bcrypt.gensalt())

    # Map the user schema to the User model
    mapped_user = map_userCreate_to_user(user)
    
    # Set the hashed password in the User model
    mapped_user.password = hashed_password.decode('utf-8')  # Store as string
    session.add(mapped_user)

    # Send the activation email to the user
    await send_email(session, user.email)

    await session.commit()
    await session.refresh(mapped_user)

    return mapped_user


# Get all users from a home grouped by role
async def get_all_users_by_home_by_role(session: AsyncSession, home_id: int):
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

    print(users_by_role)

    return UsersByRoleResponse(
        OWNER=users_by_role["OWNER"], MEMBER=users_by_role["MEMBER"]
    )


# Delete an existing user by user.id, including furniture and compartments
async def delete_user(session: AsyncSession, user_id: int):
    # Fetch the user to delete
    user = await session.get(User, user_id)

    if user is None:
        raise UserNotFoundError(f"User with ID {user_id} not found")

    # Delete the user
    await session.execute(delete(User).where(User.id == user_id))


# Update an existing user (works the same as save)
async def update_user(session: AsyncSession, user: UserCreate):
    mapped_user = map_userCreate_to_user(user)
    session.add(mapped_user)
    await session.commit()


# Find all users
async def get_all_users(session: AsyncSession):
    result = await session.execute(select(User))
    return result.scalars().all()


# Find a user by user_id
async def get_user_by_id(session: AsyncSession, user_id: int):
    stmt = select(User).where(User.id == user_id)
    result = await session.execute(stmt)
    return result.scalars().first()


# Find a user by username
async def get_user_by_username(session: AsyncSession, username: str):
    stmt = select(User).where(User.username == username)
    result = await session.execute(stmt)
    return result.scalars().first()

# Find a user by email
async def get_user_by_email(session: AsyncSession, email: str):
    stmt = select(User).where(User.email == email)
    result = await session.execute(stmt)
    return result.scalars().first()

# Activate user account
async def activate_user_account(session: AsyncSession, email: str, activation_code: str):
    # Fetch the user by email
    user = await get_user_by_email(session, email)

    print(f"ACTIVATION CODE (NOT DECODED) -> {activation_code}")
    
    if user is None:
        raise UserNotFoundError(f"User with email {email} not found")

    # Check if account is already activated
    if user.activated:
        raise AccountAlreadyActivatedError(f"User with email {email} is already activated")

    # Decode both activation codes
    decoded_activation_code = unquote(activation_code)
    stored_activation_code = unquote(user.activation_code)
    
    # Check if activation code is expired (plus 2 hours for MySQL time difference)
    activation_date = user.activation_date + datetime.timedelta(hours=2)
    expiration_time = activation_date + datetime.timedelta(minutes=5)
    
    if expiration_time < datetime.datetime.now():
        #check if this works
        await send_email(session, email)
        raise ActivationCodeExpiredError("The code used to activate the account is expired. A new link was just sent to your email")
    
    # If the activation code is correct and not expired, activate the account
    print(f"DECODED ACTIVATION CODE -> {decoded_activation_code} | USER CODE -> {stored_activation_code}")
    if decoded_activation_code == stored_activation_code:
        user.activated = True
        await session.commit()
        return True  # Account activated
    
    raise InvalidActivationCodeError("The code used to activate the account is not valid")

# Login by inputting username and password
async def login_by_user_password(session: AsyncSession, user_login: UserRequestLogin):
    user = await get_user_by_username(session, user_login.username)
    print(F"USER ACTIVATION EQUALS: {user.activated}")
    if(user.activated == False):
        raise UserNotActivatedError(f"User with username {user_login.username} is not activated. Please activate your account through the link sent to your email")
    elif(verify_password(user.password, user_login.password)):
        return user.id
    else:
        raise WrongUserCredentialsError(f"The password is incorrect. Try again.")

# Async function to update activation code in the database
async def update_activation_code(session: AsyncSession, recipient: str, activation_code: str):
    user = await get_user_by_email(session, recipient)
    if user:
        user.activation_code = activation_code 
        session.add(user)
        await session.commit()
        await session.refresh(user)
        return user.activation_code

def verify_password(stored_hash: str, input_password: str) -> bool:
    return bcrypt.checkpw(input_password.encode('utf-8'), stored_hash.encode('utf-8'))

def generate_activation_code() -> str:
    # Generate 32 random bytes
    random_bytes = secrets.token_bytes(32)
    
    # Base64 encode the random bytes
    encoded_activation_code = base64.b64encode(random_bytes).decode('utf-8')
    
    # URL-encode the Base64 string
    url_encoded_activation_code = urllib.parse.quote(encoded_activation_code, safe='')
    
    return url_encoded_activation_code


# Function to send the email
async def send_email(session: AsyncSession, recipient: str):
    try:
        # Generate activation code
        activation_code = generate_activation_code()

        # Update the activation code in the database
        new_code = await update_activation_code(session, recipient, activation_code)

        # Construct the activation link message
        message = f"{CLICK_LINK_TO_ACTIVATE_ACCOUNT} {ACTIVATE_ACCOUNT_LINK}{EMAIL_URL}{recipient}{CODE_URL}{new_code}"

        # Set up the email server and send the email
        sender_email = EMAIL_ADDRESS
        receiver_email = recipient
        password = EMAIL_PASSWORD

        # Create the email message
        msg = MIMEMultipart()
        msg['From'] = sender_email
        msg['To'] = receiver_email
        msg['Subject'] = ACTIVATE_YOUR_ACCOUNT_MAIL_SUBJECT
        msg.attach(MIMEText(message, 'plain'))

        # Set up the server and send the email
        try:
            with smtplib.SMTP('smtp.gmail.com', 587) as server:
                server.starttls()
                server.login(sender_email, password)
                server.sendmail(sender_email, receiver_email, msg.as_string())
        except Exception as e:
            print(f"Error in send_email function: {e}")  # Debugging line
            raise MailMessagingException(FAILED_TO_SEND_EMAIL_ERROR) from e

    except Exception as e:
        çprint(f"Error in send_email function: {e}")  # Debugging line
        raise MailMessagingException(FAILED_TO_SEND_EMAIL_ERROR) from e