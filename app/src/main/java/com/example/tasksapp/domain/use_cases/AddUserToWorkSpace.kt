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

class AddUserToWorkSpace @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token:String, userLogin:String, workSpaceId:String): Flow<Resource<UserModel>> = flow{
        try{
            emit(Resource.Loading<UserModel>())
            val userDTO = repository.addUserToWorkSpace(token, userLogin, workSpaceId)
            emit(Resource.Success<UserModel>(userDTO.toUserModel()))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<UserModel>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            Log.d("debugMessage", debugMessage.toString())
            emit(Resource.Error<UserModel>(message.toString()))
        }
    }
}