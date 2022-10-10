package com.example.tasksapp.data.remote

import com.example.tasksapp.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TasksApi {
    @POST("/register")
    suspend fun registerNewUser(@Body body: RegisterReceiveDTO): TokenDTO

    @POST("/login")
    suspend fun loginUser(@Body body: LoginReceiveDTO): TokenDTO

    @GET("/getUserByToken/{token}")
    suspend fun getUserByToken(@Path("token") token: String): UserDTO

    @POST("/addWorkSpace")
    suspend fun addWorkSpace(@Body body: AddWorkSpaceReceiveDTO) : WorkSpaceDTO

    @GET("/getWorkSpaces/{token}")
    suspend fun getWorkSpaces(@Path("token") token: String): List<WorkSpaceDTO>

    @GET("/getWorkSpaceById/{token}/{id}")
    suspend fun getWorkSpaceById(@Path("token") token: String, @Path("id") id: String): WorkSpaceDTO

    @GET("getTasksFromWorkSpace/{token}/{workSpaceId}")
    suspend fun getTasksFromWorkSpace(@Path("token") token: String, @Path("workSpaceId") id: String) : List<TaskDTO>

    @POST("/addTaskToWorkSpace")
    suspend fun addTaskToWorkSpace(@Body body: AddTaskReceiveDTO): TaskDTO
}