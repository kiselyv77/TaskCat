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

class GetTasksFromWorkSpaceForUser @Inject constructor(
    private val repository: TasksRepository
) {

    operator fun invoke(token: String, workSpaceId:String): Flow<Resource<List<TaskModel>>> = flow{
        try{
            emit(Resource.Loading<List<TaskModel>>())
            val tasks = repository.getTasksFromWorkSpaceForUser(token, workSpaceId)
            emit(Resource.Success<List<TaskModel>>(tasks.map { it.toTaskModel()}))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<List<TaskModel>>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<List<TaskModel>>(message.toString()))
        }
    }
}