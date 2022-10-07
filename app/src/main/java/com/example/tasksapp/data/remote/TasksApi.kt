package com.example.tasksapp.data.remote

import com.example.tasksapp.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface TasksApi {
    @POST("/register")
    suspend fun registerNewUser(@Body body: RegisterReceiveDTO): TokenDTO

    @POST("/login")
    suspend fun loginUser(@Body body: LoginReceiveDTO): TokenDTO

    @POST("/getUserByToken")
    suspend fun getUserByToken(@Body body: GetUsersReceiveDTO): UserDTO

    @POST("/addWorkSpace")
    suspend fun addWorkSpace(@Body body: AddWorkSpaceReceiveDTO) : WorkSpaceDTO

    @POST("/getWorkSpaces")
    suspend fun getWorkSpaces(@Body body: GetWorkSpacesReceiveDTO): List<WorkSpaceDTO>
}