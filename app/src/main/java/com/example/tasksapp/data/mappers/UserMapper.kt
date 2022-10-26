package com.example.tasksapp.data.mappers

import com.example.tasksapp.data.remote.dto.UserDTO
import com.example.tasksapp.domain.model.UserModel

fun UserDTO.toUserModel(): UserModel {
    return UserModel(
        name = name,
        status = status,
        login = login,
        userStatusToWorkSpace = userStatusToWorkSpace
    )
}