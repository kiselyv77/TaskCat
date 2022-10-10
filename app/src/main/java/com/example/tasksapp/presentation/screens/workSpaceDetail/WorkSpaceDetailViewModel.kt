package com.example.tasksapp.presentation.screens.workSpaceDetail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.AddTask
import com.example.tasksapp.domain.use_cases.GetTasksFromWorkSpace
import com.example.tasksapp.domain.use_cases.GetWorkSpaceById
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkSpaceDetailViewModel @Inject constructor(
    private val getWorkSpaceById: GetWorkSpaceById,
    private val getTasksFromWorkSpace: GetTasksFromWorkSpace,
    private val addTaskToWorkSpaceUseCase: AddTask,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(WorkSpaceDetailState())
    val state: State<WorkSpaceDetailState> = _state

    init {
        getWorkSpace()
        getTasks()
    }

    fun onEvent(event: WorkSpaceDetailEvent) {
        when (event) {
            is WorkSpaceDetailEvent.OnRefresh -> {
                getWorkSpace()
                getTasks()
            }
            WorkSpaceDetailEvent.OpenCloseDialog -> {
                _state.value = _state.value.copy(
                    dialogState = _state.value.dialogState.copy(isOpen = !_state.value.dialogState.isOpen)
                )
            }
            is WorkSpaceDetailEvent.SetTaskNameInDialog -> {
                _state.value = _state.value.copy(
                    dialogState = _state.value.dialogState.copy(name = event.newName)
                )
            }
            is WorkSpaceDetailEvent.SetTaskDescriptionInDialog -> {
                _state.value = _state.value.copy(
                    dialogState = _state.value.dialogState.copy(description = event.newDescription)
                )
            }
            WorkSpaceDetailEvent.AddTask -> {
                addTask()
            }
        }
    }

    private fun getWorkSpace() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            getWorkSpaceById(Token.token, id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { workSpace ->
                            _state.value = _state.value.copy(
                                workspaceDetail = workSpace,
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

    private fun getTasks() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getTasksFromWorkSpace(Token.token, workSpaceId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { tasks ->
                            _state.value = _state.value.copy(
                                tasksState = _state.value.tasksState.copy(
                                    isSuccess = true,
                                    tasks = tasks,
                                    error = "",
                                    isLoading = false
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(
                                tasksState = _state.value.tasksState.copy(
                                    error = result.message ?: "", isLoading = false
                                )
                            )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            tasksState = _state.value.tasksState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }

            }
        }
    }

    private fun addTask() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            addTaskToWorkSpaceUseCase(
                token = Token.token,
                name = _state.value.dialogState.name,
                description = _state.value.dialogState.description,
                workSpaceId = workSpaceId
            ).collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let{
                            Log.d("addWorkspace", it.toString())
                            _state.value = _state.value.copy(
                               dialogState = _state.value.dialogState.copy(
                                   isSuccess = true,
                                   isLoading = false,
                                   error = ""
                               )
                            )
                        }
                    }
                    is Resource.Error -> {
                        Log.d("addTask", result.message?:"")
                        _state.value = _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                error = result.message?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }
}