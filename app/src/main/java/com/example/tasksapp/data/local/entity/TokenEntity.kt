package com.example.tasksapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TokenEntity(
    @PrimaryKey val id:String = "token",
    val token:String,
)
