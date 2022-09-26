package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.remote.dto.TokenDTO
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetToken @Inject constructor(
    private val repository: TasksRepository

) {
    suspend operator fun invoke(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading<String>())
            val token = repository.getToken()
            emit(Resource.Success<String>(token))
        } catch (exception: Exception) {
            val debugMessage = exception.message ?: ""
            emit(Resource.Error<String>(debugMessage))
        }
    }
}