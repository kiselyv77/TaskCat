package com.example.tasksapp.util

object TaskStatus {
    const val COMPLITED_TYPE = "COMPLITED_TYPE"
    const val INPROGRESS_TYPE = "INPROGRESS_TYPE"
    const val INPLAN_TYPE = "INPLAN_TYPE"
    const val OVERDUE_TYPE = "OVERDUE_TYPE"

    const val ALL_TASKS = "ALL_TASKS"

    val TASK_TYPES = listOf<String>(COMPLITED_TYPE, INPROGRESS_TYPE, INPLAN_TYPE, OVERDUE_TYPE)

    fun getTaskStatusName(type:String): String{
        return when(type) {
            COMPLITED_TYPE -> "Выполнено"
            INPROGRESS_TYPE -> "В процессе"
            INPLAN_TYPE -> "В плане"
            OVERDUE_TYPE -> "Долги"
            else -> {"error"}
        }
    }
}