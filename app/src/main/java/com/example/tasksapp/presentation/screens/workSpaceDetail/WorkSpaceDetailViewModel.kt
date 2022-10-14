package com.example.tasksapp.presentation.screens.workSpaceDetail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.*
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkSpaceDetailViewModel @Inject constructor(
    private val getWorkSpaceById: GetWorkSpaceById,
    private val getTasksFromWorkSpace: GetTasksFromWorkSpace,
    private val addTaskToWorkSpaceUseCase: AddTask,
    private val addUserToWorkSpace: AddUserToWorkSpace,
    private val getUsersFromWorkSpace: GetUsersFromWorkSpace,
    private val setTaskStatusUseCase: SetTaskStatus,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(WorkSpaceDetailState())
    val state: State<WorkSpaceDetailState> = _state

    init {
        getWorkSpace()
        getTasks()
        getUsers()
    }

    fun onEvent(event: WorkSpaceDetailEvent) {
        when (event) {
            is WorkSpaceDetailEvent.OnAllRefresh -> {
                getWorkSpace()
                getTasks()
                getUsers()
            }
            is WorkSpaceDetailEvent.OnTasksRefresh -> {
                getTasks()
            }
            is WorkSpaceDetailEvent.OnUsersRefresh -> {
                getUsers()
            }

            is WorkSpaceDetailEvent.OpenCloseAddTaskDialog -> {
                _state.value = _state.value.copy(
                    addTaskDialogState = AddTaskDialogState().copy(isOpen = !_state.value.addTaskDialogState.isOpen)
                )
            }
            is WorkSpaceDetailEvent.OpenCloseAddUserDialog -> {
                _state.value = _state.value.copy(
                    addUserDialogState = AddUserDialogState().copy(isOpen = !_state.value.addUserDialogState.isOpen)
                )
            }
            is WorkSpaceDetailEvent.OpenCloseSetTaskStatusDialog -> {
                _state.value = _state.value.copy(
                    setTaskStatusDialogState = SetTaskStatusDialogState().copy(
                        taskId = event.taskId,
                        isOpen = !_state.value.setTaskStatusDialogState.isOpen
                    )
                )
            }
            is WorkSpaceDetailEvent.SetTaskNameInDialog -> {
                _state.value = _state.value.copy(
                    addTaskDialogState = _state.value.addTaskDialogState.copy(name = event.newName)
                )
            }
            is WorkSpaceDetailEvent.SetTaskDescriptionInDialog -> {
                _state.value = _state.value.copy(
                    addTaskDialogState = _state.value.addTaskDialogState.copy(description = event.newDescription)
                )
            }
            is WorkSpaceDetailEvent.AddTask -> {
                if(_state.value.addTaskDialogState.name.isNotEmpty()
                    &&_state.value.addTaskDialogState.description.isNotEmpty()){
                    addTask()
                }
                else{
                    _state.value = _state.value.copy(
                        addTaskDialogState = _state.value.addTaskDialogState.copy(error = "Заполните все поля")
                    )
                }
            }
            is WorkSpaceDetailEvent.AddUser -> {
                if(_state.value.addUserDialogState.userLogin.isNotEmpty()){
                    addUser()
                }
                else{
                    _state.value = _state.value.copy(
                        addUserDialogState = _state.value.addUserDialogState.copy(error = "Заполните все поля")
                    )
                }
            }
            is WorkSpaceDetailEvent.SetUserLoginInDialog -> {
                _state.value = _state.value.copy(
                    addUserDialogState = _state.value.addUserDialogState.copy(userLogin = event.newUserLogin)
                )
            }
            is WorkSpaceDetailEvent.SetTaskStatus -> {
                setTaskStatus()
            }
            is WorkSpaceDetailEvent.SetTaskStatusDialog -> {
                _state.value = _state.value.copy(
                    setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(selectedStatus = event.newStatus)
                )
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
                            Log.d("tasks", tasks.toString())
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
                name = _state.value.addTaskDialogState.name,
                description = _state.value.addTaskDialogState.description,
                workSpaceId = workSpaceId
            ).collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let{
                            Log.d("addTask", it.toString())
                            _state.value = _state.value.copy(
                               addTaskDialogState = _state.value.addTaskDialogState.copy(
                                   isSuccess = true,
                                   isLoading = false,
                                   error = ""
                               )
                            )
                            onEvent(WorkSpaceDetailEvent.OnTasksRefresh)
                        }
                    }
                    is Resource.Error -> {
                        Log.d("addTask", result.message?:"")
                        _state.value = _state.value.copy(
                            addTaskDialogState = _state.value.addTaskDialogState.copy(
                                error = result.message?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            addTaskDialogState = _state.value.addTaskDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getUsers(){
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getUsersFromWorkSpace(Token.token, workSpaceId).collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { users ->
                            _state.value = _state.value.copy(
                                usersState = _state.value.usersState.copy(
                                    isSuccess = true,
                                    users = users,
                                    error = "",
                                    isLoading = false
                                )
                            )

                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(
                                usersState = _state.value.usersState.copy(
                                    error = result.message ?: "", isLoading = false
                                )
                            )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            usersState = _state.value.usersState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }

            }
        }
    }

    private fun addUser(){
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            addUserToWorkSpace(Token.token, _state.value.addUserDialogState.userLogin, workSpaceId).collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let{
                            Log.d("addWorkspace", it.toString())
                            _state.value = _state.value.copy(
                                addUserDialogState = _state.value.addUserDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(WorkSpaceDetailEvent.OnUsersRefresh)
                        }
                    }
                    is Resource.Error -> {
                        Log.d("addTask", result.message?:"")
                        _state.value = _state.value.copy(
                            addUserDialogState = _state.value.addUserDialogState.copy(
                                error = result.message?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            addUserDialogState = _state.value.addUserDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setTaskStatus(){
        viewModelScope.launch {
            val taskId = _state.value.setTaskStatusDialogState.taskId
            val newStatus = _state.value.setTaskStatusDialogState.selectedStatus
            setTaskStatusUseCase(Token.token, taskId, newStatus).collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let{
                            _state.value = _state.value.copy(
                                setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(WorkSpaceDetailEvent.OnTasksRefresh)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                error = result.message?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
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