package com.example.tasksapp.data.repository

import com.example.tasksapp.data.local.TasksDatabase
import com.example.tasksapp.data.local.entity.TokenEntity
import com.example.tasksapp.data.remote.TasksApi
import com.example.tasksapp.data.remote.dto.*
import com.example.tasksapp.domain.repository.TasksRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream


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
        return api.getUserByToken(token)
    }

    override suspend fun addWorkSpace(
        token: String,
        name: String,
        description: String
    ): WorkSpaceDTO {
        return api.addWorkSpace(AddWorkSpaceReceiveDTO(token, name, description))
    }

    override suspend fun getWorkSpaces(token: String): List<WorkSpaceDTO> {
        return api.getWorkSpaces(token)
    }

    override suspend fun getWorkSpaceById(token: String, id: String): WorkSpaceDTO {
        return api.getWorkSpaceById(token, id)
    }

    override suspend fun getTasksFromWorkSpace(token: String, workSpaceId: String): List<TaskDTO> {
        return api.getTasksFromWorkSpace(token, workSpaceId)
    }

    override suspend fun addTaskToWorkSpace(
        token: String,
        name: String,
        description: String,
        workSpaceId: String,
        deadLine: String
    ): TaskDTO {
        return api.addTaskToWorkSpace(AddTaskReceiveDTO(token, name, description, workSpaceId, deadLine))
    }

    override suspend fun addUserToWorkSpace(
        token: String,
        userLogin: String,
        workSpaceId: String
    ): UserDTO {
        return api.addUserToWorkSpace(AddUserToWorkSpaceReceiveDTO(token, userLogin, workSpaceId))
    }

    override suspend fun getUsersFromWorkSpace(token: String, workSpaceId: String): List<UserDTO> {
        return api.getUsersFromWorkSpace(token, workSpaceId)
    }

    override suspend fun setTaskStatus(
        token: String,
        taskId: String,
        newStatus: String
    ): SuccessResponseDTO {
        return api.setTaskStatus(token, taskId, newStatus)
    }

    override suspend fun setUserStatus(token: String, newStatus: String): SuccessResponseDTO {
        return api.setUserStatus(token, newStatus)
    }

    override suspend fun getMessagesFromWorkSpace(
        token: String,
        workSpaceId: String,
        offset: String
    ): List<MessageDTO> {
        return api.getMessagesFromWorkSpace(token, workSpaceId, offset)
    }

    override suspend fun getTaskById(token: String, id: String): TaskDTO {
        return api.getTaskById(token, id)
    }

    override suspend fun setUserStatusToWorkSpace(
        token: String,
        userLogin: String,
        workSpaceId: String,
        newStatus: String
    ): SuccessResponseDTO {
        return api.setUserStatusToWorkSpace(token, userLogin, workSpaceId, newStatus)
    }

    override suspend fun uploadNewAvatar(token:String, stream: InputStream): SuccessResponseDTO {
        val part = MultipartBody.Part.createFormData(
            "newAvatar", "newAvatar", stream.readBytes().toRequestBody()
        )

        return api.uploadNewAvatar(token, part)
    }

    override suspend fun uploadFileVoiceMessage(token: String, stream: InputStream, fileName:String): SuccessResponseDTO {
        val part = MultipartBody.Part.createFormData(
            "newAvatar", fileName, stream.readBytes().toRequestBody()
        )

        return api.uploadFileVoiceMessage(token, part)
    }

    override suspend fun getNotesFromTask(token: String, taskId: String): List<NoteDTO> {
        return api.getNotesFromTask(token, taskId)
    }
}