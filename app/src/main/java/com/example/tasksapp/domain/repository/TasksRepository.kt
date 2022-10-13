package com.example.tasksapp.domain.repository

import com.example.tasksapp.data.remote.dto.TaskDTO
import com.example.tasksapp.data.remote.dto.TokenDTO
import com.example.tasksapp.data.remote.dto.UserDTO
import com.example.tasksapp.data.remote.dto.WorkSpaceDTO

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

    suspend fun setTaskStatus(token: String, taskId: String, newStatus:String):String
}