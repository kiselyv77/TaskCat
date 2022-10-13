package com.example.tasksapp.util

object TaskStatus {
    const val COMPLITED_TYPE = "COMPLITED_TYPE"
    const val INPROGRESS_TYPE = "INPROGRESS_TYPE"
    const val INPLAN_TYPE = "INPLAN_TYPE"
    const val OVERDUE_TYPE = "OVERDUE_TYPE"

    val TASK_TYPES = listOf<String>(COMPLITED_TYPE, INPROGRESS_TYPE, INPLAN_TYPE, OVERDUE_TYPE)
}