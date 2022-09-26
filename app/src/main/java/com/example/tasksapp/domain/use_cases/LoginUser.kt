package com.example.tasksapp.domain.use_cases

import com.example.tasksapp.data.remote.dto.TokenDTO
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUser @Inject constructor(
    private val repository: TasksRepository
){

    operator fun invoke(login:String ,password:String): Flow<Resource<TokenDTO>> = flow{
        try{
            emit(Resource.Loading<TokenDTO>())
            val token = repository.loginUser(login, password)
            emit(Resource.Success<TokenDTO>(token))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<TokenDTO>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<TokenDTO>(message))
        }
    }
}