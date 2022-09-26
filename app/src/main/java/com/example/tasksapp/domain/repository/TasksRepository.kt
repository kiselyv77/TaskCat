package com.example.tasksapp.domain.repository

import com.example.tasksapp.data.remote.dto.TokenDTO
import com.example.tasksapp.data.remote.dto.UserDTO

interface TasksRepository {
    suspend fun registerNewUser(name:String, login:String, password:String): TokenDTO

    suspend fun loginUser(login:String, password:String): TokenDTO

    suspend fun saveToken(token: String)

    suspend fun getToken(): String

    suspend fun deleteToken()

    suspend fun getUserByToken(token: String): UserDTO

}