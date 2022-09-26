package com.example.tasksapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasksapp.data.local.entity.TokenEntity
import javax.inject.Singleton


@Database(
    entities = [TokenEntity::class],
    version = 1
)
@Singleton
abstract class TasksDatabase: RoomDatabase() {
    abstract val dao: TasksDao
}