package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.mappers.toTaskModel
import com.example.tasksapp.domain.model.TaskModel
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetTaskById @Inject constructor(
    private val repository: TasksRepository

) {
    operator fun invoke(token: String, id:String): Flow<Resource<TaskModel>> = flow{
        try{
            emit(Resource.Loading<TaskModel>())
            val taskDTO = repository.getTaskById(token, id)
            emit(Resource.Success<TaskModel>(taskDTO.toTaskModel()))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<TaskModel>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<TaskModel>(message))
        }
    }
}