package com.example.tasksapp.domain.use_cases

import com.example.tasksapp.data.remote.dto.WorkSpaceDTO
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddWorkSpace @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token:String, name:String ,description:String): Flow<Resource<WorkSpaceDTO>> = flow{
        try{
            emit(Resource.Loading<WorkSpaceDTO>())
            val response = repository.addWorkSpace(token, name, description)
            emit(Resource.Success<WorkSpaceDTO>(response))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<WorkSpaceDTO>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<WorkSpaceDTO>(message.toString()))
        }
    }
}