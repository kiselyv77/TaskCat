package com.example.tasksapp.data.repository

import com.example.tasksapp.data.local.TasksDatabase
import com.example.tasksapp.data.local.entity.TokenEntity
import com.example.tasksapp.data.remote.TasksApi
import com.example.tasksapp.data.remote.dto.*
import com.example.tasksapp.domain.repository.TasksRepository

class TasksRepositoryImpl(
    private val api: TasksApi,
    private val db: TasksDatabase
) : TasksRepository {

    // Users Repository
    override suspend fun registerNewUser(name: String, login: String, password: String): TokenDTO {
        return api.registerNewUser(RegisterReceiveDTO(name, login, password))
    }

    override suspend fun loginUser(login: String, password: String): TokenDTO {
        return api.loginUser(LoginReceiveDTO(login, password))
    }

    // Tokens Repository
    override suspend fun saveToken(token: String) {
        db.dao.insertToken(TokenEntity(token = token))
    }

    override suspend fun getToken(): String {
        return db.dao.getToken().token
    }

    override suspend fun deleteToken() {
        db.dao.deleteToken()
    }

    override suspend fun getUserByToken(token: String): UserDTO {
        return api.getUserByToken(GetUsersReceiveDTO(value = token))
    }

    override suspend fun addWorkSpace(token: String, name: String, description: String): WorkSpaceDTO {
        return api.addWorkSpace(AddWorkSpaceReceiveDTO(token, name, description))
    }

    override suspend fun getWorkSpaces(token: String): List<WorkSpaceDTO> {
        return api.getWorkSpaces(GetWorkSpacesReceiveDTO(value = token))
    }
}