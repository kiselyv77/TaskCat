package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SetUserStatus @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token:String, newStatus:String): Flow<Resource<String>> = flow{
        try{
            emit(Resource.Loading<String>())
            val response = repository.setUserStatus(token, newStatus)
            emit(Resource.Success<String>(response.message))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<String>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            Log.d("debugMessage", debugMessage.toString())
            emit(Resource.Error<String>(message.toString()))
        }
    }
}