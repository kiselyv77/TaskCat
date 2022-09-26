package com.example.tasksapp.data.remote

import com.example.tasksapp.data.remote.dto.*
import com.example.tasksapp.domain.use_cases.GetUserByToken
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TasksApi {
    @POST("/register")
    suspend fun registerNewUser(@Body body: RegisterReceiveDTO): TokenDTO

    @POST("/login")
    suspend fun loginUser(@Body body: LoginReceiveDTO): TokenDTO

    @POST("/getUserByToken")
    suspend fun getUserByToken(@Body body: GetUsersReceiveDTO): UserDTO
}