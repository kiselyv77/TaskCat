package com.example.tasksapp.domain.use_cases

import android.util.Log
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DownloadNoteAttachmentFile @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(fileName: String): Flow<Resource<ResponseBody?>> = flow{
        try{
            emit(Resource.Loading<ResponseBody?>())
            val responseBody = repository.getNoteAttachmentFile(fileName).body()
            emit(Resource.Success<ResponseBody?>(responseBody))
        } catch (exception: HttpException){
            val debugMessage = exception.message
            val massage = exception.response()?.errorBody()?.charStream()?.readText()?:"Не удалось распознать ошибку"
            emit(Resource.Error<ResponseBody?>(massage))
        }catch (exception: IOException){
            val debugMessage = exception.message
            val message = "Ошибка подключения проверьте подключение к сети"
            Log.d("debugMessage", debugMessage.toString())
            emit(Resource.Error<ResponseBody?>(message.toString()))
        }
    }
}