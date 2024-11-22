package com.example.homeinventoryapp.data.model

import com.example.homeinventoryapp.data.model.compartment.CompartmentResponse
import com.example.homeinventoryapp.data.model.furniture.FurnitureRequestCreate
import com.example.homeinventoryapp.data.model.furniture.FurnitureRequestUpdate
import com.example.homeinventoryapp.data.model.furniture.FurnitureResponse
import com.example.homeinventoryapp.data.model.home.HomeRequestCreate
import com.example.homeinventoryapp.data.model.home.HomeResponse
import com.example.homeinventoryapp.data.model.home.MyHomesResponse
import com.example.homeinventoryapp.data.model.homeusers.HomeUsersResponse
import com.example.homeinventoryapp.data.model.invitation.InvitationResponse
import com.example.homeinventoryapp.data.model.item.ItemDetailResponse
import com.example.homeinventoryapp.data.model.item.ItemRequestCreate
import com.example.homeinventoryapp.data.model.item.ItemResponse
import com.example.homeinventoryapp.data.model.item.TagResponse
import com.example.homeinventoryapp.data.model.room.RoomRequestCreate
import com.example.homeinventoryapp.data.model.room.RoomRequestUpdate
import com.example.homeinventoryapp.data.model.room.RoomResponse
import com.example.homeinventoryapp.data.model.user.UserResponse
import com.example.homeinventoryapp.domain.model.Compartment
import com.example.homeinventoryapp.domain.model.Furniture
import com.example.homeinventoryapp.domain.model.Home
import com.example.homeinventoryapp.domain.model.HomeUsers
import com.example.homeinventoryapp.domain.model.Invitation
import com.example.homeinventoryapp.domain.model.Item
import com.example.homeinventoryapp.domain.model.ItemDetail
import com.example.homeinventoryapp.domain.model.ItemTag
import com.example.homeinventoryapp.domain.model.MyHomes
import com.example.homeinventoryapp.domain.model.Room
import com.example.homeinventoryapp.domain.model.User

fun HomeResponse.toHome(): Home {
    return Home(
        id = id,
        name = name,
        owner = owner
    )
}

fun Home.toHomeResponse(): HomeResponse {
    return HomeResponse(
        id = id,
        name = name,
        owner = owner
    )
}

fun Home.toHomeRequest(): HomeRequestCreate {
    return HomeRequestCreate(
        name = name,
        owner = owner
    )
}

fun UserResponse.toUser(): User {
    return User(
        id = id,
        username = username,
        email = email
    )
}

fun MyHomesResponse.toMyHomes(): MyHomes {
    return MyHomes(
        ownedHomes = ownedHomes.map { it.toHome() },
        memberHomes = memberHomes.map { it.toHome() }
    )
}

fun HomeUsersResponse.toHomeUsers(): HomeUsers {
    return HomeUsers(
        owner = owner.toUser(),
        members = members.map { it.toUser() }
    )
}

fun RoomResponse.toRoom(): Room {
    return Room(
        id = id,
        name = name,
        homeId = homeId
    )
}

fun Room.toRoomRequestCreate(): RoomRequestCreate {
    return RoomRequestCreate(
        name = name,
        homeId = homeId
    )
}

fun Room.toRoomRequestUpdate(): RoomRequestUpdate {
    return RoomRequestUpdate(
        id = id,
        name = name,
        homeId = homeId
    )
}

fun InvitationResponse.toInvitation(): Invitation {
    return Invitation(
        id = id,
        inviterId = inviterId,
        inviteeId = inviteeId,
        homeId = homeId
    )
}

fun FurnitureResponse.toFurniture(): Furniture {
    return Furniture(
        id = id,
        name = name,
        roomId = roomId
    )
}

fun Furniture.toFurnitureRequestCreate(): FurnitureRequestCreate {
    return FurnitureRequestCreate(
        name = name,
        roomId = roomId
    )
}

fun Furniture.toFurnitureRequestUpdate(): FurnitureRequestUpdate {
    return FurnitureRequestUpdate(
        id = id,
        name = name
    )
}

fun CompartmentResponse.toCompartment(): Compartment {
    return Compartment(
        id = id,
        name = name,
        furnId = furnId
    )
}

fun ItemResponse.toItem(): Item {
    return Item(
        id = id,
        name = name,
        compId = compId
    )
}

fun ItemDetailResponse.toItemDetail() : ItemDetail {
    return ItemDetail(
        id = id,
        name = name,
        compId = compId,
        tags = tags.map { it.toItemTag() }
    )
}

fun TagResponse.toItemTag(): ItemTag {
    return ItemTag(
        id = id,
        name = name,
        homeId = homeId
    )
}

fun ItemDetail.toItemRequestCreate(): ItemRequestCreate {
    return ItemRequestCreate(
        name = name,
        compartmentId = compId,
        tags = tags.map { it.name }
    )
}



