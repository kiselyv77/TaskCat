package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.remote.dto.WorkSpaceDTO
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWorkSpaces @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token: String): Flow<Resource<List<WorkSpaceDTO>>> = flow{
        try{
            emit(Resource.Loading<List<WorkSpaceDTO>>())
            val workSpaces = repository.getWorkSpaces(token)
            emit(Resource.Success<List<WorkSpaceDTO>>(workSpaces))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<List<WorkSpaceDTO>>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<List<WorkSpaceDTO>>(message))
        }
    }
}