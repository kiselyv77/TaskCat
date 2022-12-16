package com.example.tasksapp.domain.repository

import com.example.tasksapp.data.remote.dto.*
import java.io.InputStream

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

    suspend fun addTaskToWorkSpace(token: String, name: String, description: String, workSpaceId: String, deadLine: String, userList:List<String>): TaskDTO

    suspend fun addUserToWorkSpace(token:String, userLogin: String, workSpaceId: String): UserDTO

    suspend fun getUsersFromWorkSpace(token: String, workSpaceId: String): List<UserDTO>

    suspend fun setTaskStatus(token: String, taskId: String, newStatus:String): SuccessResponseDTO

    suspend fun setUserStatus(token: String, newStatus:String): SuccessResponseDTO

    suspend fun getMessagesFromWorkSpace(token: String, workSpaceId: String, offset:String): List<MessageDTO>

    suspend fun getTaskById(token:String, id:String): TaskDTO

    suspend fun setUserStatusToWorkSpace(token: String,userLogin:String, workSpaceId: String, newStatus:String):SuccessResponseDTO

    suspend fun uploadNewAvatar(token:String, stream: InputStream):SuccessResponseDTO

    suspend fun uploadFileVoiceMessage(token:String, stream: InputStream, fileName:String):SuccessResponseDTO

    suspend fun getNotesFromTask(token: String, taskId: String, offset: String): List<NoteDTO>

    suspend fun setDeadLine(token: String, taskId: String, newDeadLine: String): SuccessResponseDTO

    suspend fun getUsersFromTask(token: String, taskId: String): List<UserDTO>

    suspend fun addUserToTask(token: String, userLogin: String, taskId: String): SuccessResponseDTO
    suspend fun deleteWorkSpace(token: String, workSpaceId: String): SuccessResponseDTO


}