package com.example.homeinventoryapp.utils

object Constants {

    //REQUEST PATHS

    //HOMES
    const val GET_HOMES_BY_USER_PATH = "homes/{id}"
    const val GET_HOME_PATH = "homes/single/{id}"
    const val REGISTER_HOME_PATH = "homes"
    const val UPDATE_HOME_PATH = "homes"
    const val DELETE_HOME_PATH = "homes/{id}"
    const val GET_HOME_OWNERSHIP_PATH = "homes/{homeId}/ownership/{userId}"

    //USERS
    const val GET_USER_BY_USERNAME_PATH = "users/{username}"
    const val DELETE_USER_PATH = "users/{id}"
    const val GET_USER_BY_ID_PATH = "users/{id}"
    const val GET_USERS_BY_HOME_ID_PATH = "users/home/{id}"
    const val USERS_REGISTER_PATH = "users"
    const val USERS_LOGIN_PATH = "login"

    //INVITATIONS
    const val GET_INVITATION_INFO_PATH = "invitations/info/{id}"
    const val SEND_INVITATION_PATH = "invitations/send/"
    const val ACCEPT_INVITATION_PATH = "invitations/accept/{id}"
    const val DECLINE_INVITATION_PATH = "invitations/decline/{id}"
    const val GET_INVITATIONS_BY_USER = "invitations/{id}"

    //ROOMS
    const val GET_ROOMS_BY_HOME_PATH = "rooms/home/{id}"
    const val GET_ROOM_BY_ID_PATH = "rooms/{id}"
    const val REGISTER_ROOM_PATH = "rooms"
    const val UPDATE_ROOM_PATH = "rooms"
    const val DELETE_ROOM_PATH = "rooms/{id}"

    //FURNITURE
    const val GET_FURNITURE_BY_ROOM_PATH = "furniture/room/{id}"
    const val GET_FURNITURE_BY_ID_PATH = "furniture/{id}"
    const val REGISTER_FURNITURE_PATH = "furniture"
    const val UPDATE_FURNITURE_PATH = "furniture"
    const val DELETE_FURNITURE_PATH = "furniture/{id}"

    //COMPARTMENTS
    const val GET_COMPARTMENTS_BY_FURNITURE_PATH = "compartments/furniture/{id}"
    const val GET_COMPARTMENT_BY_ID_PATH = "compartments/{id}"
    const val REGISTER_COMPARTMENT_PATH = "compartments"
    const val UPDATE_COMPARTMENT_PATH = "compartments"
    const val DELETE_COMPARTMENT_PATH = "compartments/{id}"

    //ITEMS
    const val GET_ITEMS_BY_COMPARTMENT_PATH = "items/comp/{id}"
    const val GET_ITEMS_BY_STRING_PATH = "items/home/{id}"
    const val GET_ITEM_BY_ID_PATH = "items/{id}"
    const val REGISTER_ITEM_PATH = "items"
    const val UPDATE_ITEM_PATH = "items"
    const val DELETE_ITEM_PATH = "items/{id}"
    const val GET_ITEM_ROUTE_PATH = "items/route/{id}"
    const val MOVE_ITEM_ROUTE_PATH = "items/move/"

    //LOGIN-REGISTER
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"

    //REQUEST PARAMETERS
    const val ID_PARAM = "id"
    const val USERNAME_PARAM = "username"

    //ERROR MESSAGES
    const val NO_INVITATIONS_FOUND_ERROR = "This user's invitations were not found in the database"
    const val NO_USER_FOUND_ERROR = "This user was not found in the database"
    const val LOGIN_ERROR = "There was an error while logging in"
    const val REGISTER_ERROR = "There was an error while registering"
    const val NO_HOME_FOUND_ERROR = "This home was not found in the database"
    const val NO_HOMES_FOUND_ERROR = "No homes found in the database"
    const val NO_ROOM_FOUND_ERROR = "This room was not found in the database"
    const val NO_FURNITURE_FOUND_ERROR = "This piece of furniture was not found in the database"
    const val NO_FURNITURES_FOUND_ERROR = "This room's furniture was not found in the database"
    const val NO_ROOMS_FOUND_ERROR = "No rooms found in the database"
    const val NO_COMPARTMENTS_FOUND_ERROR = "No compartments found in the database"
    const val NO_ITEMS_FOUND_ERROR = "No items found in the database"
    const val NO_MEMBERS_FOUND_ERROR = "No members of this home were found in the database"
    const val DATA_NOT_FOUND_ERROR = "The data was not found"
    const val UNKNOWN_ERROR = "There was an unknown error"
    const val HOME_NOT_CREATED_ERROR = "The home was not created"
    const val HOME_NOT_DELETED_ERROR = "The home was not deleted"
    const val EMPTY_MESSAGE = ""

    //STATUS CODES
    const val STATUS_CODE_UNAUTHORIZED = "401"
    const val STATUS_CODE_FORBIDDEN = "403"
    const val STATUS_CODE_BAD_REQUEST = "400"
    const val STATUS_CODE_INTERNAL_SERVER_ERROR = "500"
    const val EMPTY_STRING = ""

    //LOGGING ERROR MESSAGES
    const val RETRIEVING_INVITATIONS_BY_USER_ERROR =
        "There was an error while retrieving the user's received invitations"
    const val RETRIEVING_INVITATION_INFO_ERROR =
        "There was an error while retrieving the invitation's information"
    const val SENDING_INVITATION_ERROR = "There was an error while sending the invitation"
    const val ACCEPTING_INVITATION_ERROR = "There was an error while accepting the invitation"
    const val DECLINING_INVITATION_ERROR = "There was an error while declining the invitation"
    const val RETRIEVING_HOMES_BY_USER_ERROR = "There was an error while retrieving homes by user"
    const val RETRIEVING_HOME_BY_ID_ERROR = "There was an error while retrieving a home by its id"
    const val RETRIEVING_FURNITURE_BY_ID_ERROR =
        "There was an error while retrieving a piece of furniture by its id"
    const val RETRIEVING_FURNITURE_BY_ROOM_ID_ERROR =
        "There was an error while retrieving this room's furniture"
    const val RETRIEVING_ROOMS_BY_HOME_ID_ERROR =
        "There was an error while retrieving the rooms in a home"
    const val RETRIEVING_ROOM_BY_ID_ERROR = "There was an error while retrieving the room by its id"
    const val RETRIEVING_COMPARTMENTS_BY_FURNITURE_ERROR =
        "There was an error while retrieving the compartments in a piece of furniture"
    const val RETRIEVING_COMPARTMENT_BY_ID_ERROR =
        "There was an error while retrieving the compartment by its id"
    const val RETRIEVING_ITEM_BY_ID_ERROR = "There was an error while retrieving the item by its id"
    const val RETRIEVING_ITEM_ROUTE_ERROR = "There was an error while retrieving the item's route"
    const val RETRIEVING_ITEMS_BY_COMPARTMENT_ERROR =
        "There was an error while retrieving the items in a specific compartment"
    const val MOVING_ITEMS_ERROR =
        "There was an error while moving items to a specific compartment"
    const val RETRIEVING_ITEMS_BY_SEARCH_WORD_ERROR =
        "There was an error while retrieving the items matching the search word"
    const val RETRIEVING_USER_BY_USERNAME_ERROR =
        "There was an error while retrieving the user by username"
    const val RETRIEVING_USER_BY_ID_ERROR = "There was an error while retrieving the user by id"
    const val RETRIEVING_USERS_BY_HOME_ID_ERROR =
        "There was an error while retrieving the users in a home"
    const val SAVING_HOME_ERROR = "There was an error while saving the home"
    const val SAVING_ROOM_ERROR = "There was an error while saving the room"
    const val SAVING_FURNITURE_ERROR = "There was an error while saving the piece of furniture"
    const val SAVING_COMPARTMENT_ERROR = "There was an error while saving the compartment"
    const val SAVING_ITEM_ERROR = "There was an error while saving the item"
    const val UPDATING_HOME_ERROR = "There was an error while updating the home"
    const val UPDATING_ROOM_ERROR = "There was an error while updating the room"
    const val UPDATING_FURNITURE_ERROR = "There was an error while updating the piece of furniture"
    const val UPDATING_COMPARTMENT_ERROR = "There was an error while updating the compartment"
    const val UPDATING_ITEM_ERROR = "There was an error while saving the item"
    const val DELETING_USER_ERROR = "There was an error while deleting the user"
    const val DELETING_HOME_ERROR = "There was an error while deleting the home"
    const val DELETING_ROOM_ERROR = "There was an error while deleting the room"
    const val DELETING_FURNITURE_ERROR = "There was an error while deleting the piece of furniture"
    const val DELETING_COMPARTMENT_ERROR = "There was an error while deleting the compartment"
    const val DELETING_ITEM_ERROR = "There was an error while deleting the item"
    const val RETRIEVING_HOME_OWNERSHIP_ERROR = "There was an error while retrieving the home's ownership status"

    //UI ERROR MESSAGES
    const val PERMISSION_DENIED_ERROR =
        "ACCESS DENIED - ONLY USERS WITH GRANTED ACCESS CAN ACCESS THIS FEATURE"
    const val ACTIVATION_REQUIRED_ERROR =
        "PLEASE ACTIVATE YOUR ACCOUNT - CHECK YOUR EMAIL FOR ACTIVATION LINK"
    const val WRONG_LOGIN_INFO_ERROR = "ACCESS DENIED - WRONG USERNAME OR PASSWORD"
    const val ACCOUNT_NOT_ACTIVATED_ERROR =
        "ACCOUNT NOT ACTIVATED - PLEASE ACTIVATE YOUR ACCOUNT BEFORE LOGGING IN"
    const val USERNAME_OR_EMAIL_ALREADY_EXISTS_ERROR =
        "USERNAME OR EMAIL ALREADY EXISTS - PLEASE TRY REGISTERING AGAIN WITH DIFFERENT CREDENTIALS"

    //SCREEN ROUTES
    const val MY_HOMES_ROUTE = "my_homes_screen"
    const val HOME_ROUTE = "home_screen/{homeId}"
    const val HOME_SCREEN_ROUTE = "home_screen"
    const val ACCOUNT_ROUTE = "account_screen"
    const val ROOMS_ROUTE = "rooms_screen/{homeId}"
    const val ROOM_ROUTE = "room_screen/{roomId}"
    const val SEARCH_ITEMS_ROUTE = "search_items_screen"
    const val ITEM_ROUTE = "item_screen/{itemId}"
    const val FURNITURE_ROUTE = "furniture_screen/{furnId}"
    const val COMPARTMENT_ROUTE = "compartment_screen/{compId}"

    //SCREEN NAMES
    const val LOGIN_BAR_NAME = "Login"
    const val REGISTER_BAR_NAME = "Register"
    const val HOME_BAR_NAME = "Home"
    const val HOME_SCREEN_BAR_NAME = "Home Screen"
    const val MY_HOMES_BAR_NAME = "All of my homes"
    const val ACCOUNT_BAR_NAME = "Personal Profile"
    const val ROOMS_BAR_NAME = "Rooms"
    const val ROOM_BAR_NAME = "Room"
    const val FURNITURE_BAR_NAME = "Furniture"
    const val COMPARTMENT_BAR_NAME = "Compartment"
    const val ITEM_BAR_NAME = "Item"
    const val SEARCH_ITEMS_BAR_NAME = "Search Items"

    //NAVIGATION VARIABLES
    const val HOME_ID = "homeId"
    const val ROOM_ID = "roomId"
    const val FURN_ID = "furnId"
    const val COMP_ID = "compId"
    const val ITEM_ID = "itemId"
    const val USER_ID = "userId"

    //USE-CASE CONSTANTS
    const val ZERO_CODE = 0
}