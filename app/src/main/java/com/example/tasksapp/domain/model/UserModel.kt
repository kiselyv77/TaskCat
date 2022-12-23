package com.example.tasksapp.domain.model

data class UserModel(
    val name: String,
    val status: String,
    val login: String,
    val userStatusToWorkSpace:String,
    val userStatusToTask: String
){
    companion object {
        fun getEmptyModel():UserModel{
            return UserModel(
                name = "",
                status = "",
                login = "",
                userStatusToWorkSpace = "",
                userStatusToTask = ""
            )
        }
    }
}
