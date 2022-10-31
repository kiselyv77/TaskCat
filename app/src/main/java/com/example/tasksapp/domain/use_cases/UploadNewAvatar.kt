package com.example.tasksapp.domain.use_cases

import android.net.Uri
import com.example.tasksapp.domain.repository.TasksRepository
import com.example.tasksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UploadNewAvatar @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(token:String, imageUri: Uri?): Flow<Resource<String>> = flow{
        try{
            emit(Resource.Loading<String>())
            val response = repository.uploadNewAvatar(token, imageUri)
            emit(Resource.Success<String>(response.message))
        } catch (exception: Exception) {
            val debugMessage = exception.message
            emit(Resource.Error<String>(debugMessage.toString()))
        }
    }
}