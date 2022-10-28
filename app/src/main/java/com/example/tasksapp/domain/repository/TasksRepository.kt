package com.example.tasksapp.domain.repository

import com.example.tasksapp.data.remote.dto.*

interface TasksRepository {
    suspend fun registerNewUser(name:String, login:String, password:String): TokenDTO

    suspend fun loginUser(login:String, password:String): TokenDTO

    suspend fun saveToken(token: String)

    suspend fun getToken(): String

    suspend fun deleteToken()

    suspend fun getUserByToken(token: String): UserDTO

    suspend fun addWorkSpace(token: String, name: String, description: String): WorkSpaceDTO

    suspend fun getWorkSpaces(token: String): List<WorkSpaceDTO>

    suspend fun getWorkSpaceById(token:String, id:String):WorkSpaceDTO

    suspend fun getTasksFromWorkSpace(token: String, workSpaceId: String): List<TaskDTO>

    suspend fun addTaskToWorkSpace(token: String, name: String, description: String, workSpaceId: String): TaskDTO

    suspend fun addUserToWorkSpace(token:String, userLogin: String, workSpaceId: String): UserDTO

    suspend fun getUsersFromWorkSpace(token: String, workSpaceId: String): List<UserDTO>

    suspend fun setTaskStatus(token: String, taskId: String, newStatus:String): SuccessResponseDTO

    suspend fun setUserStatus(token: String, newStatus:String): SuccessResponseDTO

    suspend fun getMessagesFromWorkSpace(token: String, workSpaceId: String, offset:String): List<MessageDTO>

    suspend fun getTaskById(token:String, id:String): TaskDTO

    suspend fun setUserStatusToWorkSpace(token: String,userLogin:String, workSpaceId: String, newStatus:String):SuccessResponseDTO
}