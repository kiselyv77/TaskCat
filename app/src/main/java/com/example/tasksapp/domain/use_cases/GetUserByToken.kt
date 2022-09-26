package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.remote.dto.TokenDTO
import com.example.tasksapp.data.remote.dto.UserDTO
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserByToken @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(token :String): Flow<Resource<UserDTO>> = flow {
        try {
            emit(Resource.Loading<UserDTO>())
            val user = repository.getUserByToken(token)
            emit(Resource.Success<UserDTO>(user))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<UserDTO>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<UserDTO>(message))
        }
    }
}