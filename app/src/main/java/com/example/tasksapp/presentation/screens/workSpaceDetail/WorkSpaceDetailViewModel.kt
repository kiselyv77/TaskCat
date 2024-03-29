package com.example.tasksapp.presentation.screens.workSpaceDetail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.*
import com.example.tasksapp.presentation.commonComponents.CustomAlertDialogState
import com.example.tasksapp.presentation.commonComponents.SetTaskStatusDialogState
import com.example.tasksapp.util.Resource
import com.example.tasksapp.util.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WorkSpaceDetailViewModel @Inject constructor(
    private val getWorkSpaceById: GetWorkSpaceById,
    private val getTasksFromWorkSpace: GetTasksFromWorkSpace,
    private val getTasksFromWorkSpaceForUser: GetTasksFromWorkSpaceForUser,
    private val addTaskToWorkSpaceUseCase: AddTask,
    private val addUserToWorkSpace: AddUserToWorkSpace,
    private val getUsersFromWorkSpace: GetUsersFromWorkSpace,
    private val deleteWorkSpaceUseCase: DeleteWorkSpace,
    private val deleteUserFromWorkSpaceUseCase: DeleteUserFromWorkSpace,
    private val setTaskStatusUseCase: SetTaskStatus,
    private val getUserByToken: GetUserByToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(WorkSpaceDetailState())
    val state: State<WorkSpaceDetailState> = _state

    init {
        getWorkSpace()
        getTasks()
        getUsers()
        getMyLogin()
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
                    addTaskDialogState = AddTaskDialogState().copy(
                        isOpen = !_state.value.addTaskDialogState.isOpen,
                        users = _state.value.usersState.users.filter { it.login != state.value.myLogin }
                    )
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
                        selectedStatus = _state.value.tasksState.tasks.lastOrNull { it.id == event.taskId }?.taskStatus
                            ?: "",
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
                if (_state.value.addTaskDialogState.name.isNotEmpty()
                    && _state.value.addTaskDialogState.description.isNotEmpty()
                ) {
                    if (_state.value.addTaskDialogState.deadLine != LocalDateTime.MIN) addTask()
                    else _state.value = _state.value.copy(
                        addTaskDialogState = _state.value.addTaskDialogState.copy(error = "Назначте сроки выполнения")
                    )

                } else {
                    _state.value = _state.value.copy(
                        addTaskDialogState = _state.value.addTaskDialogState.copy(error = "Заполните все поля")
                    )
                }
            }
            is WorkSpaceDetailEvent.AddUser -> {
                if (_state.value.addUserDialogState.userLogin.isNotEmpty()) {
                    addUser()
                } else {
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
                    setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                        selectedStatus = event.newStatus
                    )
                )
            }
            is WorkSpaceDetailEvent.SetTasksFilter -> {
                Log.d("filter", event.filter)
                _state.value = _state.value.copy(
                    tasksState = _state.value.tasksState.copy(
                        selectedTasksFilter = event.filter,
                        filteredTasks =
                        if (event.filter == TaskStatus.ALL_TASKS) _state.value.tasksState.tasks
                        else _state.value.tasksState.tasks.filter { it.taskStatus == event.filter }
                    ),
                )
            }
            is WorkSpaceDetailEvent.SetTaskDeadLineDialog -> {
                _state.value = _state.value.copy(
                    addTaskDialogState = _state.value.addTaskDialogState.copy(deadLine = event.deadLine)
                )
            }
            is WorkSpaceDetailEvent.UserSelectDialog -> {
                _state.value = _state.value.copy(
                    addTaskDialogState = _state.value.addTaskDialogState.copy(

                        selectedUsers = if (_state.value.addTaskDialogState.selectedUsers.contains(
                                event.userLogin
                            )
                        ) {
                            _state.value.addTaskDialogState.selectedUsers.filter { it != event.userLogin }
                        } else {
                            _state.value.addTaskDialogState.selectedUsers.plus(event.userLogin)
                        }
                    )
                )
            }

            is WorkSpaceDetailEvent.OpenCloseDeleteWorkSpaceDialog -> {
                if (_state.value.deleteWorkSpaceDialog.isOpen) {
                    _state.value = _state.value.copy(deleteWorkSpaceDialog = CustomAlertDialogState())
                } else {
                    _state.value = _state.value.copy(deleteWorkSpaceDialog = CustomAlertDialogState(isOpen = true))
                }
            }
            is WorkSpaceDetailEvent.DeleteWorkSpace -> deleteWorkSpace()

            is WorkSpaceDetailEvent.OpenCloseLeaveDialog -> {
                if (_state.value.leaveDialog.isOpen) {
                    _state.value = _state.value.copy(leaveDialog = CustomAlertDialogState())
                } else {
                    _state.value = _state.value.copy(leaveDialog = CustomAlertDialogState(isOpen = true))
                }
            }
            is WorkSpaceDetailEvent.Leave -> {
                leave()
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
            getTasksFromWorkSpaceForUser(Token.token, workSpaceId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { tasks ->
                            Log.d("tassasavaadsdsvsdcvsdvsdvks", tasks.toString())
                            _state.value = _state.value.copy(
                                tasksState = _state.value.tasksState.copy(
                                    isSuccess = true,
                                    tasks = tasks,
                                    error = "",
                                    isLoading = false,
                                    filteredTasks =
                                    if (_state.value.tasksState.selectedTasksFilter == TaskStatus.ALL_TASKS) tasks
                                    else tasks.filter { it.taskStatus == _state.value.tasksState.selectedTasksFilter }
                                )
                            )

                        }
                    }
                    is Resource.Error -> {
                        Log.d("tassasavaadsdsvsdcvsdvsdvks", result.message.toString())
                        _state.value =
                            _state.value.copy(
                                tasksState = _state.value.tasksState.copy(
                                    error = result.message ?: "", isLoading = false
                                )
                            )
                    }
                    is Resource.Loading -> {
                        Log.d("tassasavaadsdsvsdcvsdvsdvks", result.message.toString())
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
                workSpaceId = workSpaceId,
                deadLine = _state.value.addTaskDialogState.deadLine.format(DateTimeFormatter.ISO_DATE_TIME),
                userList = _state.value.addTaskDialogState.selectedUsers
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
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
                        Log.d("addTask", result.message ?: "")
                        _state.value = _state.value.copy(
                            addTaskDialogState = _state.value.addTaskDialogState.copy(
                                error = result.message ?: "",
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

    private fun getUsers() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getUsersFromWorkSpace(Token.token, workSpaceId).collect { result ->
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

    private fun addUser() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            addUserToWorkSpace(
                Token.token,
                _state.value.addUserDialogState.userLogin,
                workSpaceId
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { it ->
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
                        Log.d("addUser2321323", result.message ?: "")
                        _state.value = _state.value.copy(
                            addUserDialogState = _state.value.addUserDialogState.copy(
                                error = result.message ?: "",
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

    private fun setTaskStatus() {
        viewModelScope.launch {
            val taskId = _state.value.setTaskStatusDialogState.taskId
            val newStatus = _state.value.setTaskStatusDialogState.selectedStatus
            setTaskStatusUseCase(Token.token, taskId, newStatus).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
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
                                error = result.message ?: "",
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

    private fun deleteWorkSpace() {
        viewModelScope.launch {
            deleteWorkSpaceUseCase(Token.token, _state.value.workspaceDetail.id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                deleteWorkSpaceDialog = _state.value.deleteWorkSpaceDialog.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            deleteWorkSpaceDialog = _state.value.deleteWorkSpaceDialog.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            deleteWorkSpaceDialog = _state.value.deleteWorkSpaceDialog.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun leave() {
        viewModelScope.launch {
            deleteUserFromWorkSpaceUseCase(Token.token, _state.value.workspaceDetail.id, _state.value.myLogin).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                leaveDialog = _state.value.leaveDialog.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            leaveDialog = _state.value.leaveDialog.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            leaveDialog = _state.value.leaveDialog.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMyLogin() {
        viewModelScope.launch {
            getUserByToken(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { userDto ->
                            _state.value = _state.value.copy(
                                myLogin = userDto.login,
                                error = result.message ?: "",
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