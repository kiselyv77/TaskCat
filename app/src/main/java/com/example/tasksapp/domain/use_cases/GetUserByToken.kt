package com.example.tasksapp.domain.use_cases

import com.example.tasksapp.data.mappers.toUserModel
import com.example.tasksapp.domain.model.UserModel
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
    suspend operator fun invoke(token :String): Flow<Resource<UserModel>> = flow {
        try {
            emit(Resource.Loading<UserModel>())
            val userDTO = repository.getUserByToken(token)
            emit(Resource.Success<UserModel>(userDTO.toUserModel()))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<UserModel>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<UserModel>(message))
        }
    }
}