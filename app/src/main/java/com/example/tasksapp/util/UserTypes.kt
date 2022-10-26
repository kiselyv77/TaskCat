package com.example.tasksapp.util

object UserTypes {
    const val CREATOR_TYPE = "CREATOR_TYPE"
    const val ADMIN_TYPE = "ADMIN_TYPE"
    const val MEMBER_TYPE = "MEMBER_TYPE"

    fun getUserTypeName(type:String): String{
        return when(type) {
            CREATOR_TYPE -> "Создатель"
            ADMIN_TYPE -> "Администратор"
            MEMBER_TYPE -> "Сотрудник"
            else -> {"error"}
        }
    }
}