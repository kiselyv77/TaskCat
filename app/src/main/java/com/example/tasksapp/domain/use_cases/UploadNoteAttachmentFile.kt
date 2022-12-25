package com.example.tasksapp.domain.use_cases

import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import javax.inject.Inject

class UploadNoteAttachmentFile @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token: String, stream: InputStream, fileName: String): Flow<Resource<String>> = flow{
        try{
            emit(Resource.Loading<String>())
            val response = repository.uploadNoteAttachmentFile(token, stream, fileName)
            emit(Resource.Success<String>(response.message))
        } catch (exception: Exception) {
            val debugMessage = exception.message
            emit(Resource.Error<String>(debugMessage.toString()))
        }
    }
}