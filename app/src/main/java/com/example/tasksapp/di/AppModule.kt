package com.example.tasksapp.di

import android.app.Application
import androidx.room.Room
import com.example.tasksapp.data.local.TasksDatabase
import com.example.tasksapp.data.remote.TasksApi
import com.example.tasksapp.data.repository.TasksRepositoryImpl
import com.example.tasksapp.domain.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCryptoApi(): TasksApi {
        return Retrofit.Builder()
            .baseUrl("https://dd11-194-28-28-173.eu.ngrok.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TasksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTasksDatabase(app: Application): TasksDatabase {
        return Room.databaseBuilder(
            app,
            TasksDatabase::class.java,
            "tasksdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api:TasksApi, tasksDatabase: TasksDatabase): TasksRepository {
        return TasksRepositoryImpl(api, tasksDatabase)
    }

}