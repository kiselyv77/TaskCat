package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.mappers.toUserModel
import com.example.tasksapp.domain.model.UserModel
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUsersFromTask @Inject constructor(
    val repository: TasksRepository
) {
    operator fun invoke(token: String, taskId:String): Flow<Resource<List<UserModel>>> = flow{
        try{
            emit(Resource.Loading<List<UserModel>>())
            val users = repository.getUsersFromTask(token, taskId)
            emit(Resource.Success<List<UserModel>>(users.map{it.toUserModel()}))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<List<UserModel>>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<List<UserModel>>(message))
        }
    }
}