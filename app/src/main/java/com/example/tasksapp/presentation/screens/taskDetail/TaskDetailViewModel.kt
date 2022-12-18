package com.example.tasksapp.presentation.screens.taskDetail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.data.mappers.toNoteModel
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.data.remote.dto.NoteDTO
import com.example.tasksapp.domain.use_cases.*
import com.example.tasksapp.presentation.commonComponents.CustomAlertDialogState
import com.example.tasksapp.presentation.commonComponents.SetTaskStatusDialogState
import com.example.tasksapp.util.Resource
import com.example.tasksapp.util.generateRandomUUID
import com.example.tasksapp.util.getIsoDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getUserByToken: GetUserByToken,
    private val getTaskByIdUseCase: GetTaskById,
    private val getNotesFromTask: GetNotesFromTask,
    private val setTaskStatusUseCase: SetTaskStatus,
    private val setTaskDeadLineUseCase: SetTaskDeadLine,
    private val getUsersFromTaskUseCase: GetUsersFromTask,
    private val getUsersFromWorkSpace: GetUsersFromWorkSpace,
    private val addUserToTaskUseCase: AddUserToTask,
    private val deleteTaskUseCase: DeleteTask,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(TaskDetailState())
    val state: State<TaskDetailState> = _state

    private var client: HttpClient = HttpClient {
        install(WebSockets) {
            contentConverter = GsonWebsocketContentConverter()
        }
    }
    private val _notesFlow = MutableSharedFlow<SendNote>()
    private var offset =
        -10 // Этот офсет будет увеличиватся на 10 при каждом новом запросе или на 1 приприеме сообщения

    init {
        getMyLogin()
        getTask()
        getUsers()
        viewModelScope.launch(Dispatchers.IO) {
            val taskId = savedStateHandle.get<String>("id") ?: return@launch
            try {
                client.ws("ws://${Spec.BASE_URL}/notes/${Token.token}/$taskId") {
                    val messageOutputRoutine = launch {
                        while (true) {
                            val note = receiveDeserialized<NoteDTO>()
                            if (!_state.value.notesList.any { it.id == note.id }) {
                                //Пришло чужое сообщение
                                offset++
                                val messageList = _state.value.notesList.toMutableList()
                                messageList.add(0, note.toNoteModel())
                                _state.value = _state.value.copy(notesList = messageList)
                            } else {
                                //Вернулось мое сообщение
                                offset++
                                val messageList = _state.value.notesList.toMutableList()
                                val messageModel = note.toNoteModel()
                                val index =
                                    messageList.indexOf(messageModel.copy(isArrived = false))
                                messageList[index] = messageModel.copy(isArrived = true)
                                _state.value = _state.value.copy(notesList = messageList)
                            }
                        }
                    }
                    val userInputRoutine = launch {
                        _notesFlow.collectLatest { sendNote ->
                            val noteId = sendNote.attachmentFile
                            val noteDTO = NoteDTO(
                                id = noteId,
                                info = sendNote.info,
                                loginUser = _state.value.my.login,
                                userName = _state.value.my.name,
                                taskId = taskId,
                                attachmentFile = "",
                                dateTime = getIsoDateTime()
                            )
                            val messageList = _state.value.notesList.toMutableList()
                            messageList.add(
                                0,
                                noteDTO.toNoteModel().copy(isArrived = false)
                            )
                            _state.value = _state.value.copy(notesList = messageList)

                            sendSerialized(
                                noteDTO
                            )
                        }
                    }
                    userInputRoutine.join() // Wait for completion; either "exit" or error
                    messageOutputRoutine.cancelAndJoin()
                }
            } catch (e: ClosedReceiveChannelException) {
                Log.w("WebSocketSession", "Failure: ${e.message}")
            } catch (e: NoTransformationFoundException) {
                //Возникает если слишком часто открывать и закрывать чат
                // причина 429 Too Many Requests
                Log.w("WebSocketSession", "Failure: ${e.message}")
            } catch (e: Exception) {
                Log.w("WebSocketSession", "Failure: ${e.message}")
            }
        }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.SendNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _notesFlow.emit(
                        SendNote(
                            info = _state.value.inputText,
                            attachmentFile = generateRandomUUID()
                        )
                    )
                    _state.value = _state.value.copy(inputText = "")
                }
            }
            is TaskDetailEvent.OnAllRefresh -> {
                getTask()
            }
            is TaskDetailEvent.SetInputText -> {
                _state.value = _state.value.copy(inputText = event.newText)
            }
            TaskDetailEvent.ShowMore -> {
                getMyLogin()
            }
            is TaskDetailEvent.OpenCloseSetTaskStatusDialog -> {
                _state.value = _state.value.copy(
                    setTaskStatusDialogState = SetTaskStatusDialogState().copy(
                        selectedStatus = _state.value.task.taskStatus,
                        taskId = _state.value.task.id,
                        isOpen = !_state.value.setTaskStatusDialogState.isOpen
                    )
                )
            }
            TaskDetailEvent.SetTaskStatus -> {
                setTaskStatus()
            }
            is TaskDetailEvent.SetTaskStatusDialog -> {
                _state.value = _state.value.copy(
                    setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                        selectedStatus = event.newStatus
                    )
                )
            }
            is TaskDetailEvent.SetTaskDeadLine -> {
                setTaskDeadLine(event.newDeadLine.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            }

            TaskDetailEvent.OnUsersRefresh -> {
                getUsers()
            }
            TaskDetailEvent.OpenCloseAddUserToTaskDialog -> {
                if (_state.value.addUserDialogState.isOpen) {
                    _state.value = _state.value.copy(
                        addUserDialogState = AddUserToTaskDialogState(),
                    )
                } else {
                    getUsersFromWorkSpace()
                    _state.value = _state.value.copy(
                        addUserDialogState = AddUserToTaskDialogState(isOpen = true)
                    )
                }
            }
            is TaskDetailEvent.AddUserToTask -> {
                addUser(userLogin = event.userLogin)
            }
            TaskDetailEvent.DeleteTask -> {
                deleteTask()
            }
            TaskDetailEvent.OpenCloseDeleteTaskDialog -> {
                if (_state.value.deleteTaskDialog.isOpen) {
                    _state.value = _state.value.copy(
                        deleteTaskDialog = CustomAlertDialogState(),
                    )
                } else {
                    getUsersFromWorkSpace()
                    _state.value = _state.value.copy(
                        deleteTaskDialog = CustomAlertDialogState(isOpen = true)
                    )
                }
            }
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase(Token.token, _state.value.task.id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                deleteTaskDialog = _state.value.deleteTaskDialog.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            deleteTaskDialog = _state.value.deleteTaskDialog.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            deleteTaskDialog = _state.value.deleteTaskDialog.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
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

    private fun getMyLogin() {
        viewModelScope.launch() {
            getUserByToken(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(error = "", isLoading = false)
                        result.data?.let { userDto ->
                            _state.value = _state.value.copy(
                                my = userDto,
                            )
                            getNotes()
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message.toString(), isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(error = "", isLoading = true)
                    }
                }
            }
        }
    }

    private fun getNotes() {
        offset += 10
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getNotesFromTask(Token.token, workSpaceId, offset).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val notesList = _state.value.notesList.toMutableList()
                        result.data?.let { messages ->
                            notesList.addAll(messages)
                            _state.value = _state.value.copy(
                                notesList = notesList,
                                error = result.message ?: "",
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(
                                error = result.message ?: "", isLoading = false
                            )
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
                            onEvent(TaskDetailEvent.OnAllRefresh)
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

    private fun setTaskDeadLine(newDeadLine: String) {
        viewModelScope.launch {
            val taskId = _state.value.task.id
            setTaskDeadLineUseCase(Token.token, taskId, newDeadLine).collect { result ->
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
                            onEvent(TaskDetailEvent.OnAllRefresh)
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

    private fun getUsers() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getUsersFromTaskUseCase(Token.token, workSpaceId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { users ->
                            _state.value = _state.value.copy(
                                usersState = _state.value.usersState.copy(
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

    private fun getUsersFromWorkSpace() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("workSpaceId") ?: return@launch
            getUsersFromWorkSpace(Token.token, workSpaceId).collect { result ->
                Log.d("asdasdadssddfdlg,s", result.toString() )
                when (result) {

                    is Resource.Success -> {
                        result.data?.let { users ->
                            Log.d("asdasdasdsdasadsdasda", users.toString())
                            _state.value = _state.value.copy(
                                addUserDialogState = _state.value.addUserDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    displayedList =
                                       users.filter { it.login !in  _state.value.usersState.users.map { item -> item.login } },
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            addUserDialogState = _state.value.addUserDialogState.copy(
                                error = result.message ?: "", isLoading = false
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

    private fun addUser(userLogin: String) {
        viewModelScope.launch {
            val taskId = _state.value.task.id
            addUserToTaskUseCase(
                Token.token,
                userLogin,
                taskId
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { it ->
                            Log.d("addWorkspace", it.toString())
                            _state.value = _state.value.copy(
                                addUserDialogState = _state.value.addUserDialogState.copy(
                                    displayedList = _state.value.addUserDialogState.displayedList.filter { it.login != userLogin }
                                )
                            )
                            onEvent(TaskDetailEvent.OnUsersRefresh)
                        }
                    }
                    is Resource.Error -> {
                        Log.d("addUser2321323", result.message ?: "")
                        _state.value = _state.value.copy(

                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(

                        )
                    }
                }
            }
        }
    }
}