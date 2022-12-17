package com.example.tasksapp.data.remote

import com.example.tasksapp.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.http.*


interface TasksApi {
    @POST("/register")
    suspend fun registerNewUser(@Body body: RegisterReceiveDTO): TokenDTO

    @POST("/login")
    suspend fun loginUser(@Body body: LoginReceiveDTO): TokenDTO

    @GET("/getUserByToken/{token}")
    suspend fun getUserByToken(@Path("token") token: String): UserDTO

    @POST("/addWorkSpace")
    suspend fun addWorkSpace(@Body body: AddWorkSpaceReceiveDTO): WorkSpaceDTO

    @GET("/getWorkSpaces/{token}")
    suspend fun getWorkSpaces(@Path("token") token: String): List<WorkSpaceDTO>

    @GET("/getWorkSpaceById/{token}/{id}")
    suspend fun getWorkSpaceById(@Path("token") token: String, @Path("id") id: String): WorkSpaceDTO

    @GET("getTasksFromWorkSpace/{token}/{workSpaceId}")
    suspend fun getTasksFromWorkSpace(
        @Path("token") token: String,
        @Path("workSpaceId") workSpaceId: String
    ): List<TaskDTO>

    @POST("/addTaskToWorkSpace")
    suspend fun addTaskToWorkSpace(@Body body: AddTaskReceiveDTO): TaskDTO

    @POST("/addUserToWorkSpace")
    suspend fun addUserToWorkSpace(@Body body: AddUserToWorkSpaceReceiveDTO): UserDTO

    @GET("/getUsersFromWorkSpace/{token}/{workSpaceId}")
    suspend fun getUsersFromWorkSpace(
        @Path("token") token: String,
        @Path("workSpaceId") workSpaceId: String
    ): List<UserDTO>

    @POST("/setTaskStatus/{token}/{taskId}/{newStatus}")
    suspend fun setTaskStatus(
        @Path("token") token: String,
        @Path("taskId") taskId: String,
        @Path("newStatus") newStatus: String
    ): SuccessResponseDTO

    @POST("/setUserStatus/{token}/{newStatus}")
    suspend fun setUserStatus(
        @Path("token") token: String,
        @Path("newStatus") newStatus: String
    ): SuccessResponseDTO

    @GET("/getMessagesFromWorkSpace/{token}/{workSpaceId}/{offset}")
    suspend fun getMessagesFromWorkSpace(
        @Path("token") token: String,
        @Path("workSpaceId") workSpaceId: String,
        @Path("offset") offset: String
    ): List<MessageDTO>

    @GET("/getTaskById/{token}/{id}")
    suspend fun getTaskById(@Path("token") token: String, @Path("id") id: String): TaskDTO

    @POST("/setUserStatusToWorkSpace/{token}/{userLogin}/{workSpaceId}/{newStatus}")
    suspend fun setUserStatusToWorkSpace(
        @Path("token") token: String,
        @Path("userLogin") userLogin: String,
        @Path("workSpaceId") workSpaceId: String,
        @Path("newStatus") newStatus: String
    ): SuccessResponseDTO

    @Multipart
    @POST("/uploadNewAvatar/{token}")
    suspend fun uploadNewAvatar(
        @Path("token") token: String,
        @Part part: MultipartBody.Part
    ): SuccessResponseDTO

    @Multipart
    @POST("/uploadFileVoiceMessage/{token}")
    suspend fun uploadFileVoiceMessage(
        @Path("token") token: String,
        @Part part: MultipartBody.Part
    ): SuccessResponseDTO

    @GET("/getNotesFromTask/{token}/{taskId}/{offset}")
    suspend fun getNotesFromTask(
        @Path("token") token: String,
        @Path("taskId") taskId: String,
        @Path("offset") offset: String
    ): List<NoteDTO>

    @POST("/setTaskDeadLine/{token}/{taskId}/{newDeadLine}")
    suspend fun setTaskDeadLine(
        @Path("token") token: String,
        @Path("taskId") taskId: String,
        @Path("newDeadLine") newDeadLine: String
    ): SuccessResponseDTO

    @GET("/getUsersFromTask/{token}/{taskId}")
    suspend fun getUsersFromTask(
        @Path("token") token: String,
        @Path("taskId") taskId: String
    ): List<UserDTO>

    @POST("/addUserToTask/{token}/{userLogin}/{taskId}")
    suspend fun addUserToTask(
        @Path("token") token: String,
        @Path("userLogin") userLogin: String,
        @Path("taskId") taskId: String
    ): SuccessResponseDTO

    @POST("/deleteWorkSpace/{token}/{workSpaceId}")
    suspend fun deleteWorkSpace(
        @Path("token") token: String,
        @Path("workSpaceId") workSpaceId: String
    ): SuccessResponseDTO


    @POST("/deleteUserFromWorkSpace/{token}/{workSpaceId}/{userLogin}")
    suspend fun deleteUserFromWorkSpace(
        @Path("token") token: String,
        @Path("workSpaceId") workSpaceId: String,
        @Path("userLogin") userLogin: String
    ): SuccessResponseDTO


    @POST("/deleteUserFromTask/{token}/{taskId}/{userLogin}")
    suspend fun deleteUserFromTask(
        @Path("token") token: String,
        @Path("taskId") taskId: String,
        @Path("userLogin") userLogin: String
    ): SuccessResponseDTO
}