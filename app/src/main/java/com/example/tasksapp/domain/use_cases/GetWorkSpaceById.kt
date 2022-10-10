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

class GetWorkSpaceById @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token: String, id:String): Flow<Resource<WorkSpaceDTO>> = flow{
        try{
            emit(Resource.Loading<WorkSpaceDTO>())
            val workSpaces = repository.getWorkSpaceById(token, id)
            emit(Resource.Success<WorkSpaceDTO>(workSpaces))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<WorkSpaceDTO>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<WorkSpaceDTO>(message))
        }
    }
}