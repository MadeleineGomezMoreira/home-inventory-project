package com.example.homeinventoryapp.utils

object Constants {

    //REQUEST PATHS

    //HOMES
    const val GET_HOMES_BY_USER_PATH = "homes/{id}"
    const val GET_HOME_PATH = "homes/single/{id}"
    const val REGISTER_HOME_PATH = "homes"
    const val UPDATE_HOME_PATH = "homes"
    const val DELETE_HOME_PATH = "homes/{id}"

    //USERS
    const val GET_USER_BY_USERNAME_PATH = "users/{username}"
    const val GET_USER_BY_ID_PATH = "users/{id}"
    const val GET_USERS_BY_HOME_ID_PATH = "users/home/{id}"

    //INVITATIONS
    const val SEND_INVITATION_PATH = "invitations/send"
    const val ACCEPT_INVITATION_PATH = "invitations/accept/{id}"
    const val DECLINE_INVITATION_PATH =  "invitations/decline/{id}"
    const val GET_INVITATIONS_BY_USER =  "invitations/{id}"

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
    const val GET_COMPARTMENTS_BY_FURNITURE_PATH = "compartments/{id}"
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

    //REQUEST PARAMETERS
    const val ID_PARAM = "id"
    const val USERNAME_PARAM = "username"

    //ERROR MESSAGES
    const val NO_USER_FOUND_ERROR = "This user was not found in the database"
    const val NO_HOME_FOUND_ERROR = "This home was not found in the database"
    const val NO_HOMES_FOUND_ERROR = "No homes found in the database"
    const val NO_ROOM_FOUND_ERROR = "This room was not found in the database"
    const val NO_FURNITURE_FOUND_ERROR = "This piece of furniture was not found in the database"
    const val NO_FURNITURES_FOUND_ERROR = "This room's furniture was not found in the database"
    const val NO_ROOMS_FOUND_ERROR = "No rooms found in the database"
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

    //LOGGING ERROR MESSAGES
    const val RETRIEVING_HOMES_BY_USER_ERROR = "There was an error while retrieving homes by user"
    const val RETRIEVING_HOME_BY_ID_ERROR = "There was an error while retrieving a home by its id"
    const val RETRIEVING_FURNITURE_BY_ID_ERROR = "There was an error while retrieving a piece of furniture by its id"
    const val RETRIEVING_FURNITURE_BY_ROOM_ID_ERROR = "There was an error while retrieving this room's furniture"
    const val RETRIEVING_ROOMS_BY_HOME_ID_ERROR = "There was an error while retrieving the rooms in a home"
    const val RETRIEVING_ROOM_BY_ID_ERROR = "There was an error while retrieving the room by its id"
    const val RETRIEVING_USER_BY_USERNAME_ERROR =
        "There was an error while retrieving the user by username"
    const val RETRIEVING_USER_BY_ID_ERROR = "There was an error while retrieving the user by id"
    const val RETRIEVING_USERS_BY_HOME_ID_ERROR =
        "There was an error while retrieving the users in a home"
    const val SAVING_HOME_ERROR = "There was an error while saving the home"
    const val SAVING_ROOM_ERROR = "There was an error while saving the room"
    const val SAVING_FURNITURE_ERROR = "There was an error while saving the piece of furniture"
    const val UPDATING_ROOM_ERROR = "There was an error while updating the room"
    const val UPDATING_FURNITURE_ERROR = "There was an error while updating the piece of furniture"
    const val DELETING_HOME_ERROR = "There was an error while deleting the home"
    const val DELETING_ROOM_ERROR = "There was an error while deleting the room"
    const val DELETING_FURNITURE_ERROR = "There was an error while deleting the piece of furniture"

    //UI ERROR MESSAGES
    const val PERMISSION_DENIED_ERROR =
        "ACCESS DENIED - ONLY USERS WITH GRANTED ACCESS CAN ACCESS THIS FEATURE"

    //SCREEN ROUTES
    const val MY_HOMES_ROUTE = "my_homes_screen"
    const val HOME_ROUTE = "home_screen/{homeId}"
    const val HOME_SCREEN_ROUTE = "home_screen"
    const val ACCOUNT_ROUTE = "account_screen/{userId}"
    const val ROOMS_ROUTE = "rooms_screen/{homeId}"
    const val SEARCH_ITEMS_ROUTE = "search_items_screen/{homeId}"

    //SCREEN NAMES (BOTTOM BAR)
    const val HOME_BAR_NAME = "Home"
    const val HOME_SCREEN_BAR_NAME = "Home Screen"
    const val MY_HOMES_BAR_NAME = "All of my homes"
    const val ACCOUNT_BAR_NAME = "Personal Profile"
    const val ROOMS_BAR_NAME = "Rooms"
    const val SEARCH_ITEMS_BAR_NAME = "Search Items"

    //NAVIGATION VARIABLES
    const val HOME_ID = "homeId"
}