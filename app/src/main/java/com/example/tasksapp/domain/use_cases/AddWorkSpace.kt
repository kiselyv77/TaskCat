package com.example.tasksapp.domain.use_cases

import com.example.tasksapp.data.mappers.toWorkspaceModel
import com.example.tasksapp.domain.model.WorkSpaceModel
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
    operator fun invoke(token:String, name:String ,description:String): Flow<Resource<WorkSpaceModel>> = flow{
        try{
            emit(Resource.Loading<WorkSpaceModel>())
            val workSpaceDTO = repository.addWorkSpace(token, name, description)
            emit(Resource.Success<WorkSpaceModel>(workSpaceDTO.toWorkspaceModel()))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<WorkSpaceModel>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<WorkSpaceModel>(message.toString()))
        }
    }
}