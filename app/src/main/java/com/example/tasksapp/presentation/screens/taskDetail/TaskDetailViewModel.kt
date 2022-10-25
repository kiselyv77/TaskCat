package com.example.tasksapp.presentation.screens.taskDetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.GetTaskById
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskById,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = mutableStateOf(TaskDetailState())
    val state: State<TaskDetailState> = _state

    init {
        getTask()
    }

    fun onEvent(event:TaskDetailEvent){

    }

    private fun getTask() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            getTaskByIdUseCase(Token.token, id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { task ->
                            _state.value = _state.value.copy(
                                task = task,
                                error = "",
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message ?: "", isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading,
                            error = result.message ?: ""
                        )
                    }
                }

            }
        }
    }

}