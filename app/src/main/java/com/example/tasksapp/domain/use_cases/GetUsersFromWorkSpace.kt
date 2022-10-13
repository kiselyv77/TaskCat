package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.remote.dto.UserDTO
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUsersFromWorkSpace @Inject constructor(
    val repository: TasksRepository
) {
    operator fun invoke(token: String, workSpaceId:String): Flow<Resource<List<UserDTO>>> = flow{
        try{
            emit(Resource.Loading<List<UserDTO>>())
            val users = repository.getUsersFromWorkSpace(token, workSpaceId)
            emit(Resource.Success<List<UserDTO>>(users))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<List<UserDTO>>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<List<UserDTO>>(message))
        }
    }
}