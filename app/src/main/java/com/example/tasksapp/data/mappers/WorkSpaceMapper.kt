package com.example.tasksapp.data.mappers

import com.example.tasksapp.data.remote.dto.WorkSpaceDTO
import com.example.tasksapp.domain.model.WorkSpaceModel

fun WorkSpaceDTO.toWorkspaceModel(): WorkSpaceModel {
    return WorkSpaceModel(
        id = id,
        name = name,
        description = description,
        creator = creator,
    )
}