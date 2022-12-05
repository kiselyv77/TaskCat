package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.data.mappers.toNoteModel
import com.example.tasksapp.domain.model.NoteModel
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNotesFromTask @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token: String, taskId: String, offset: Int): Flow<Resource<List<NoteModel>>> = flow{
        try{
            emit(Resource.Loading<List<NoteModel>>())
            val notes = repository.getNotesFromTask(token, taskId, offset.toString())
            emit(Resource.Success<List<NoteModel>>(notes.map { it.toNoteModel()}))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<List<NoteModel>>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            Log.d("debugMessage", debugMessage.toString())
            val message = "Ошибка подключения проверьте подключение к сети"
            emit(Resource.Error<List<NoteModel>>(message.toString()))
        }
    }
}