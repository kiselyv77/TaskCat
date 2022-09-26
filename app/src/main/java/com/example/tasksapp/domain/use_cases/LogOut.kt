package com.example.tasksapp.domain.use_cases

import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogOut @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(): Flow<Resource<String>> = flow {
        try{
            emit(Resource.Loading<String>())
            repository.deleteToken()
            Token.token = ""
            emit(Resource.Success<String>("Токен удален"))
        } catch (exception: Exception){
            val debugMessage = exception.message?:""
            emit(Resource.Error<String>(debugMessage))
        }
    }
}